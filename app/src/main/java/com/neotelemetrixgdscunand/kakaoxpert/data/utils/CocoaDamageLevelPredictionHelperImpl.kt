package com.neotelemetrixgdscunand.kakaoxpert.data.utils

import android.content.Context
import android.graphics.Bitmap
import androidx.core.net.toUri
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.BoundingBox
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.CocoaDisease
import com.neotelemetrixgdscunand.kakaoxpert.domain.presentation.CocoaDamageLevelPredictionHelper
import com.neotelemetrixgdscunand.kakaoxpert.domain.presentation.CocoaDamageLevelPredictionResult
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.withContext
import org.tensorflow.lite.DataType
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.common.FileUtil
import org.tensorflow.lite.support.common.ops.CastOp
import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.image.ops.TransformToGrayscaleOp
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.File
import javax.inject.Inject

class CocoaDamageLevelPredictionHelperImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val imageConverter: ImageConverter
) : CocoaDamageLevelPredictionHelper {

    private var blackpodModelInterpreter: Interpreter? = null
    private var helopeltisModelInterpreter: Interpreter? = null
    private var podborerModelInterpreter: Interpreter? = null

    private var tensorWidth = 0
    private var tensorHeight = 0
    private var numChannel = 0
    //private var numElements = 0

    override suspend fun setup() = withContext(Dispatchers.Default) {
        cleanResource()

        var model = FileUtil.loadMappedFile(context, BLACKPOD_MODEL_PATH)
        ensureActive()

        val options = Interpreter.Options()
        options.numThreads = 3

        blackpodModelInterpreter = Interpreter(model, options)

        ensureActive()

        model = FileUtil.loadMappedFile(context, HELOPELTIS_MODEL_PATH)
        helopeltisModelInterpreter = Interpreter(model, options)
        ensureActive()

        model = FileUtil.loadMappedFile(context, PODBORER_MODEL_PATH)
        podborerModelInterpreter = Interpreter(model, options)
        ensureActive()

        val inputShape = blackpodModelInterpreter?.getInputTensor(0)?.shape() ?: return@withContext

        tensorWidth = inputShape[1]
        tensorHeight = inputShape[2]
        numChannel = inputShape[3]
    }

    override suspend fun predict(
        imagePath: String,
        boundingBoxes: List<BoundingBox>
    ): CocoaDamageLevelPredictionResult = withContext(Dispatchers.Default) {
        val isNeedSetupDetectorFirst =
            blackpodModelInterpreter == null || helopeltisModelInterpreter == null || podborerModelInterpreter == null || tensorWidth == 0 || tensorHeight == 0 || numChannel == 0
        if (isNeedSetupDetectorFirst) {
            ensureActive()
            setup()
        }

        val imageProcessor = ImageProcessor.Builder()
            .add(ResizeOp(tensorHeight, tensorWidth, ResizeOp.ResizeMethod.BILINEAR))
            .add(TransformToGrayscaleOp())
            .add(NormalizeOp(INPUT_MEAN, INPUT_STANDARD_DEVIATION))
            .add(CastOp(INPUT_IMAGE_TYPE))
            .build()

        val predictedDamageLevelList = mutableListOf<Float>()

        ensureActive()
        val imageFile = File(imagePath)
        val imageUri = imageFile.toUri()
        val imageBitmap = imageConverter.convertImageUriToBitmap(imageUri)

        boundingBoxes.forEach { currentBoundingBox ->
            ensureActive()
            val croppedBoundingBoxImage = cropBoundingBoxFromBitmap(imageBitmap, currentBoundingBox)

            ensureActive()
            val tensorImage = TensorImage(DataType.FLOAT32)
            tensorImage.load(croppedBoundingBoxImage)

            ensureActive()
            val processedImage = imageProcessor.process(tensorImage)
            val imageBuffer = processedImage.buffer

            val output = TensorBuffer.createFixedSize(
                intArrayOf(
                    1,
                    numChannel
                ),
                OUTPUT_IMAGE_TYPE
            )

            ensureActive()
            try {
                val disease = CocoaDisease.getDiseaseFromName(currentBoundingBox.label)

                when (disease) {
                    CocoaDisease.BLACKPOD -> blackpodModelInterpreter?.run(
                        imageBuffer,
                        output.buffer
                    )

                    CocoaDisease.HELOPELTIS -> helopeltisModelInterpreter?.run(
                        imageBuffer,
                        output.buffer
                    )

                    CocoaDisease.POD_BORER -> podborerModelInterpreter?.run(
                        imageBuffer,
                        output.buffer
                    )

                    else -> throw IllegalArgumentException("Invalid disease type")
                }

                val predictedPrice = output.floatArray.first()
                predictedDamageLevelList.add(predictedPrice)

            } catch (e: Exception) {
                if (e is CancellationException) throw e

                return@withContext CocoaDamageLevelPredictionResult.Error(exception = e)
            }
        }
        CocoaDamageLevelPredictionResult.Success(predictedDamageLevelList)
    }

    override fun cleanResource() {
        blackpodModelInterpreter?.close()
        helopeltisModelInterpreter?.close()
        podborerModelInterpreter?.close()

        blackpodModelInterpreter = null
        helopeltisModelInterpreter = null
        podborerModelInterpreter = null
    }

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

//    fun saveBitmapToInternalStorage(context: Context, bitmap: Bitmap, filename: String = "croping.jpg"): File {
//        // Save to internal app-specific storage: /data/data/<package>/files/
//        val file = File(context.filesDir, filename)
//
//        FileOutputStream(file).use { outputStream ->
//            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
//        }
//
//        return file // return the File object in case you want to log the path
//    }

    companion object {
        private val INPUT_IMAGE_TYPE = DataType.FLOAT32
        private val OUTPUT_IMAGE_TYPE = DataType.FLOAT32
        const val INPUT_MEAN = 0f
        const val INPUT_STANDARD_DEVIATION = 1f
        private const val BLACKPOD_MODEL_PATH = "regression_blackpod.tflite"
        private const val HELOPELTIS_MODEL_PATH = "regression_helopeltis.tflite"
        private const val PODBORER_MODEL_PATH = "regression_podborer.tflite"
    }
}