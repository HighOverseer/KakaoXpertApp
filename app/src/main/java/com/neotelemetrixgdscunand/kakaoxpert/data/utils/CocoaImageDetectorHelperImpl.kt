package com.neotelemetrixgdscunand.kakaoxpert.data.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import androidx.core.net.toUri
import androidx.exifinterface.media.ExifInterface
import com.neotelemetrixgdscunand.kakaoxpert.domain.presentation.BoundingBoxProcessor
import com.neotelemetrixgdscunand.kakaoxpert.domain.presentation.CocoaImageDetectorHelper
import com.neotelemetrixgdscunand.kakaoxpert.domain.presentation.CocoaImageDetectorHelper.Companion.INPUT_MEAN
import com.neotelemetrixgdscunand.kakaoxpert.domain.presentation.CocoaImageDetectorHelper.Companion.INPUT_STANDARD_DEVIATION
import com.neotelemetrixgdscunand.kakaoxpert.domain.presentation.CocoaImageDetectorHelper.Companion.TEMP_CLASSES
import com.neotelemetrixgdscunand.kakaoxpert.domain.presentation.ImageDetectorResult
import com.neotelemetrixgdscunand.kakaoxpert.domain.presentation.ModelLabelExtractor
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
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.File
import javax.inject.Inject

class CocoaImageDetectorHelperImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val imageConverter: ImageConverter,
    private val modelLabelExtractor: ModelLabelExtractor
) : CocoaImageDetectorHelper {

    private var interpreter: Interpreter? = null
    private var labels = mutableListOf<String>()

    private var tensorWidth = 0
    private var tensorHeight = 0
    private var numChannel = 0
    private var numElements = 0

    private val imageProcessor = ImageProcessor.Builder()
        .add(NormalizeOp(INPUT_MEAN, INPUT_STANDARD_DEVIATION))
        .add(CastOp(INPUT_IMAGE_TYPE))
        .build()


    override suspend fun setupImageDetector() = withContext(Dispatchers.Default) {
        clearResource()

        val model = FileUtil.loadMappedFile(context, MODEL_PATH)
        ensureActive()
        val options = Interpreter.Options()
        options.numThreads = 6
        interpreter = Interpreter(model, options)

        val inputShape = interpreter?.getInputTensor(0)?.shape() ?: return@withContext
        val outputShape = interpreter?.getOutputTensor(0)?.shape() ?: return@withContext

        tensorWidth = inputShape[1]
        tensorHeight = inputShape[2]
        numChannel = outputShape[1]
        numElements = outputShape[2]

        if (labels.isEmpty()) {
            ensureActive()
            labels.addAll(modelLabelExtractor.extractNamesFromMetadata(MODEL_PATH))

            if (labels.isNotEmpty()) return@withContext

            ensureActive()
            labels.addAll(modelLabelExtractor.extractNamesFromLabelFile(LABEL_PATH))

            if (labels.isNotEmpty()) return@withContext

            labels.addAll(TEMP_CLASSES)
        }
    }

    override fun clearResource() {
        interpreter?.close()
        interpreter = null
    }

    override suspend fun detect(imagePath: String): ImageDetectorResult =
        withContext(Dispatchers.Default) {
            val isNeedSetupDetectorFirst =
                interpreter == null || tensorWidth == 0 || tensorHeight == 0 || numChannel == 0 || numElements == 0
            if (isNeedSetupDetectorFirst) {
                ensureActive()
                setupImageDetector()
            }

            ensureActive()
            val imageFile = File(imagePath)
            val imageUri = imageFile.toUri()
            ensureActive()
            val imageBitmap = imageConverter.convertImageUriToBitmap(imageUri)
                .run {
                    val resizedBitmap =
                        Bitmap.createScaledBitmap(this, tensorWidth, tensorHeight, false)
                    ensureActive()
                    rotateBitmapIfRequired(resizedBitmap, imageFile)
                }

            ensureActive()
            val tensorImage = TensorImage(DataType.FLOAT32)
            tensorImage.load(imageBitmap)

            ensureActive()
            val processedImage = imageProcessor.process(tensorImage)
            val imageBuffer = processedImage.buffer

            val output = TensorBuffer.createFixedSize(
                intArrayOf(
                    1,
                    numChannel,
                    numElements
                ),
                OUTPUT_IMAGE_TYPE
            )

            ensureActive()
            try {
                interpreter?.run(imageBuffer, output.buffer)
                val bestBoxes = BoundingBoxProcessor.getBoundingBox(
                    modelOutputFloatArray = output.floatArray,
                    numChannels = numChannel,
                    numElements = numElements,
                    labels = labels
                )

                ensureActive()
                if (bestBoxes != null) {
                    return@withContext ImageDetectorResult.Success(bestBoxes)
                } else return@withContext ImageDetectorResult.NoObjectDetected
            } catch (e: Exception) {
                if (e is CancellationException) throw e

                return@withContext ImageDetectorResult.Error(e)
            }

        }


    companion object {
        private val INPUT_IMAGE_TYPE = DataType.FLOAT32
        private val OUTPUT_IMAGE_TYPE = DataType.FLOAT32
        private const val MODEL_PATH = "rev_3.tflite"
        private const val LABEL_PATH: String = "labels.txt"
    }

}

fun rotateBitmapIfRequired(bitmap: Bitmap, imageFile: File): Bitmap {
    val ei = ExifInterface(imageFile.absolutePath)
    val orientation = ei.getAttributeInt(
        ExifInterface.TAG_ORIENTATION,
        ExifInterface.ORIENTATION_NORMAL
    )

    return when (orientation) {
        ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(bitmap, 90f)
        ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(bitmap, 180f)
        ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(bitmap, 270f)
        else -> bitmap
    }
}

private fun rotateImage(source: Bitmap, angle: Float): Bitmap {
    val matrix = Matrix()
    matrix.postRotate(angle)
    return Bitmap.createBitmap(
        source, 0, 0, source.width, source.height, matrix, true
    )
}

//fun saveBitmapToFile(context: Context, bitmap: Bitmap, filename: String): File? {
//    val directory = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "SavedImages")
//    if (!directory.exists()) {
//        directory.mkdirs()
//    }
//
//    val file = File(directory, "$filename.png")
//
//    return try {
//        val outputStream = FileOutputStream(file)
//        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
//        outputStream.flush()
//        outputStream.close()
//        file
//    } catch (e: IOException) {
//        e.printStackTrace()
//        null
//    }
//}