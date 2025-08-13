package com.neotelemetrixgdscunand.kakaoxpert.data.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import androidx.core.net.toUri
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.BoundingBox
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.CocoaDisease
import com.neotelemetrixgdscunand.kakaoxpert.domain.presentation.CocoaDamageLevelPredictionHelper
import com.neotelemetrixgdscunand.kakaoxpert.domain.presentation.CocoaDamageLevelPredictionResult
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.analysissessionresult.util.roundOffDecimal
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.common.FileUtil
import java.io.File
import java.nio.ByteBuffer
import java.nio.ByteOrder
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException
import kotlin.math.exp

class CocoaDamageSeverityPredictionHelperImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val imageConverter: ImageConverter
):CocoaDamageLevelPredictionHelper {

    private var blackpodModelInterpreter: Interpreter? = null
    private var helopeltisModelInterpreter: Interpreter? = null
    private var podborerModelInterpreter: Interpreter? = null

    private val mapCocoaDiseaseToInterpreter by lazy {
        hashMapOf(
            CocoaDisease.BLACKPOD to blackpodModelInterpreter,
            CocoaDisease.HELOPELTIS to helopeltisModelInterpreter,
            CocoaDisease.POD_BORER to podborerModelInterpreter
        )
    }

    override suspend fun setup() = withContext(Dispatchers.Default) {
        cleanResource()

        var model = FileUtil.loadMappedFile(context,
            BLACKPOD_MODEL_PATH
        )
        ensureActive()

        val options = Interpreter.Options()
        //options.numThreads = 3
        options.setNumThreads(3)

        blackpodModelInterpreter = Interpreter(model, options)

        ensureActive()

        model = FileUtil.loadMappedFile(context,
            HELOPELTIS_MODEL_PATH
        )
        helopeltisModelInterpreter = Interpreter(model, options)
        ensureActive()

        model = FileUtil.loadMappedFile(context,
            PODBORER_MODEL_PATH
        )
        podborerModelInterpreter = Interpreter(model, options)
        ensureActive()

    }

    override suspend fun predict(
        imagePath: String,
        boundingBoxes: List<BoundingBox>
    ): CocoaDamageLevelPredictionResult = withContext(Dispatchers.Default){
        val isNeedSetupDetectorFirst =
            blackpodModelInterpreter == null || helopeltisModelInterpreter == null || podborerModelInterpreter == null
        if (isNeedSetupDetectorFirst) {
            ensureActive()
            setup()
        }

        val predictedDamageSeverityList = mutableListOf<Float>()

        ensureActive()
        val imageFile = File(imagePath)
        val imageUri = imageFile.toUri()
        val imageBitmap = imageConverter.convertImageUriToBitmap(imageUri)

        boundingBoxes.forEach { currentBoundingBox ->
            ensureActive()
            val croppedBoundingBoxImage = cropBoundingBoxFromBitmap(imageBitmap, currentBoundingBox)

            ensureActive()
            try {
                val disease = CocoaDisease.getDiseaseFromName(currentBoundingBox.label)

                if (disease == CocoaDisease.NONE) {
                    predictedDamageSeverityList.add(0f)
                    return@forEach
                }

                val interpreter = mapCocoaDiseaseToInterpreter[disease]
                val fruitAreaPixelsCount =
                    interpreter?.let { calculateFruitAreaPixels(croppedBoundingBoxImage, it, disease) } ?: 1

                val infectedAreaPixelsCount =
                    interpreter?.let { calculateInfectedAreaPixels(croppedBoundingBoxImage, it) } ?: 0

                val predictedDamageSeverity = (infectedAreaPixelsCount.toFloat() / (infectedAreaPixelsCount + fruitAreaPixelsCount))
                    .coerceIn(0f, 1f).also { println(it) }
                    .times(100)
                    .roundOffDecimal(2)

                predictedDamageSeverityList.add(predictedDamageSeverity)

            } catch (e: Exception) {
                if (e is CancellationException) throw e

                return@withContext CocoaDamageLevelPredictionResult.Error(exception = e)
            }
        }
        CocoaDamageLevelPredictionResult.Success(predictedDamageSeverityList)
    }

    override fun cleanResource() {
        blackpodModelInterpreter?.close()
        helopeltisModelInterpreter?.close()
        podborerModelInterpreter?.close()

        blackpodModelInterpreter = null
        helopeltisModelInterpreter = null
        podborerModelInterpreter = null
    }


    private suspend fun calculateInfectedAreaPixels(imageBitmap: Bitmap, interpreter: Interpreter): Int = withContext(Dispatchers.Default) {

        val inputImage = Bitmap.createScaledBitmap(imageBitmap, 640, 640, true)

        ensureActive()
        val inputBuffer = ByteBuffer.allocateDirect(4 * 640 * 640 * 3)
            .order(ByteOrder.nativeOrder())

        for (y in 0 until 640) {
            for (x in 0 until 640) {
                val pixel = inputImage.getPixel(x, y)

                inputBuffer.putFloat(Color.red(pixel) / 255.0f)
                inputBuffer.putFloat(Color.green(pixel) / 255.0f)
                inputBuffer.putFloat(Color.blue(pixel) / 255.0f)
            }
        }

        //val tensorImage = TensorImage.fromBitmap(inputImage)
        //val inputBuffer = tensorImage.buffer
        ensureActive()
        val output0 = Array(1) { Array(38) { FloatArray(8400) } }
        val output1 = Array(1) { Array(160) { Array(160) { FloatArray(32) } } }

        interpreter.runForMultipleInputsOutputs(arrayOf(inputBuffer), mapOf(0 to output0, 1 to output1))

        val dets = output0[0] // [38][8400]
        val proto = output1[0] // [160][160][32]

        ensureActive()
        // ✅ Transpose dets: from [38][8400] to [8400][38]
        val transposedDets = Array(8400) { FloatArray(38) }
        for (i in 0 until 38) {
            for (j in 0 until 8400) {
                transposedDets[j][i] = dets[i][j]
            }
        }

        ensureActive()
        // ✅ Find best box using objectness score at index 4
        var bestIdx = 0
        var bestScore = 0f
        for (i in transposedDets.indices) {
            val score = transposedDets[i][4]
            if (score > bestScore) {
                bestScore = score
                bestIdx = i
            }
        }

        val bestDet = transposedDets[bestIdx]

        // ✅ Extract last 31 mask coefficients from bestDet
        val maskCoeffs = bestDet.copyOfRange(7, 7 + 31)

        ensureActive()
        // ✅ Reconstruct mask
        val mask = Array(160) { FloatArray(160) }
        for (y in 0 until 160) {
            for (x in 0 until 160) {
                var sum = 0f
                for (c in 0 until 31) {
                    sum += proto[y][x][c] * maskCoeffs[c]
                }
                mask[y][x] = 1f / (1f + exp(-sum))
            }
        }

        ensureActive()
        var infectedAreaPixelsCount = 0
        for (y in 0 until 160) {
            for (x in 0 until 160) {
                if (mask[y][x] > 0.1f) infectedAreaPixelsCount++
            }
        }

        infectedAreaPixelsCount
    }


    private suspend fun calculateFruitAreaPixels(imageBitmap: Bitmap, interpreter: Interpreter, disease: CocoaDisease): Int = withContext(Dispatchers.Default){
        val inputImage = Bitmap.createScaledBitmap(imageBitmap, 640, 640, true)

        ensureActive()
        val inputBuffer = ByteBuffer.allocateDirect(4 * 640 * 640 * 3)
            .order(ByteOrder.nativeOrder())

        for (y in 0 until 640) {
            for (x in 0 until 640) {
                val pixel = inputImage.getPixel(x, y)

                inputBuffer.putFloat(Color.red(pixel) / 255.0f)
                inputBuffer.putFloat(Color.green(pixel) / 255.0f)
                inputBuffer.putFloat(Color.blue(pixel) / 255.0f)
            }
        }

        ensureActive()

        //val tensorImage = TensorImage.fromBitmap(inputImage)
        //val inputBuffer = tensorImage.buffer

        val output0 = Array(1) { Array(38) { FloatArray(8400) } }
        val output1 = Array(1) { Array(160) { Array(160) { FloatArray(32) } } }

        interpreter.runForMultipleInputsOutputs(arrayOf(inputBuffer), mapOf(0 to output0, 1 to output1))

        val dets = output0[0] // [38][8400]
        val proto = output1[0] // [160][160][32]

        ensureActive()
        // ✅ Transpose dets: from [38][8400] to [8400][38]
        val transposedDets = Array(8400) { FloatArray(38) }
        for (i in 0 until 38) {
            for (j in 0 until 8400) {
                transposedDets[j][i] = dets[i][j]
            }
        }

        // ✅ Find best box using objectness score at index 4
        ensureActive()
        var bestIdx = 0
        var bestScore = 0f
        for (i in transposedDets.indices) {
            val score = transposedDets[i][4]
            if (score > bestScore) {
                bestScore = score
                bestIdx = i
            }
        }

        val bestDet = transposedDets[bestIdx]

        // ✅ Extract last 31 mask coefficients from bestDet
        val maskCoeffs = bestDet.copyOfRange(6, 6 + 31)

        // ✅ Reconstruct mask
        ensureActive()
        val mask = Array(160) { FloatArray(160) }
        for (y in 0 until 160) {
            for (x in 0 until 160) {
                var sum = 0f
                for (c in 0 until 31) {
                    sum += proto[y][x][c] * maskCoeffs[c]
                }
                mask[y][x] = 1f / (1f + exp(-sum))
            }
        }

        val thresholdFruitAreaMask = mapDiseaseToThresholdFruitAreaMask[disease] ?: 0.99f
        var fruitAreaPixelsCount = 0
        ensureActive()
        for (y in 0 until 160) {
            for (x in 0 until 160) {
                if (mask[y][x] >= thresholdFruitAreaMask) fruitAreaPixelsCount++
            }
        }

        fruitAreaPixelsCount
    }

    //H 0.995f
    //PBK 0.99f
    private val mapDiseaseToThresholdFruitAreaMask = hashMapOf(
        CocoaDisease.BLACKPOD to 0.8f,
        CocoaDisease.HELOPELTIS to 0.995f,
        CocoaDisease.POD_BORER to 0.99f
    )


    private fun cropBoundingBoxFromBitmap(
        bitmap: Bitmap,
        box: BoundingBox
    ): Bitmap {
        val left = (box.x1 * bitmap.width).toInt().coerceIn(0, bitmap.width - 1)
        val top = (box.y1 * bitmap.height).toInt().coerceIn(0, bitmap.height - 1)
        val right = (box.x2 * bitmap.width).toInt().coerceIn(left + 1, bitmap.width)
        val bottom = (box.y2 * bitmap.height).toInt().coerceIn(top + 1, bitmap.height)

        val width = right - left
        val height = bottom - top

        return Bitmap.createBitmap(bitmap, left, top, width, height)
    }

    companion object {
        private const val BLACKPOD_MODEL_PATH = "model_seg_blackpod.tflite"
        private const val HELOPELTIS_MODEL_PATH = "model_seg_helopeltis.tflite"
        private const val PODBORER_MODEL_PATH = "model_seg_pbk.tflite"
    }
}