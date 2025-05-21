package com.neotelemetrixgdscunand.kakaoxpert.data.utils

import android.content.Context
import android.graphics.Bitmap
import androidx.core.net.toUri
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.BoundingBox
import com.neotelemetrixgdscunand.kakaoxpert.domain.presentation.CocoaPredictionResult
import com.neotelemetrixgdscunand.kakaoxpert.domain.presentation.CocoaPricePredictionHelper
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
import java.io.FileOutputStream
import javax.inject.Inject

class CocoaPricePredictionHelperImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val imageConverter: ImageConverter
): CocoaPricePredictionHelper {

    private var interpreter: Interpreter? = null

    private var tensorWidth = 0
    private var tensorHeight = 0
    private var numChannel = 0
    //private var numElements = 0

    override suspend fun setup() = withContext(Dispatchers.Default){
        cleanResource()

        val model = FileUtil.loadMappedFile(context, MODEL_PATH)
        ensureActive()

        val options = Interpreter.Options()
        options.numThreads = 3

        interpreter = Interpreter(model, options)

        val inputShape = interpreter?.getInputTensor(0)?.shape() ?: return@withContext

        tensorWidth = inputShape[1]
        tensorHeight = inputShape[2]
        numChannel = inputShape[3]
    }

    override suspend fun predict(imagePath: String, boundingBoxes:List<BoundingBox>):CocoaPredictionResult
    = withContext(Dispatchers.Default) {
        val isNeedSetupDetectorFirst =
            interpreter == null || tensorWidth == 0 || tensorHeight == 0 || numChannel == 0
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

        val predictedPrices = mutableListOf<Float>()

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
                interpreter?.run(imageBuffer, output.buffer)
                val predictedPrice = output.floatArray.first()
                predictedPrices.add(predictedPrice)

            }catch (e:Exception){
                if(e is CancellationException) throw e

                return@withContext CocoaPredictionResult.Error(exception = e)
            }
        }
        CocoaPredictionResult.Success(predictedPrices)
    }

    override fun cleanResource() {
        interpreter?.close()
        interpreter = null
    }

    private fun cropBoundingBoxFromBitmap(
        bitmap: Bitmap,
        box: BoundingBox
    ):Bitmap{
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

    companion object{
        private val INPUT_IMAGE_TYPE = DataType.FLOAT32
        private val OUTPUT_IMAGE_TYPE = DataType.FLOAT32
        const val INPUT_MEAN = 0f
        const val INPUT_STANDARD_DEVIATION = 255f
        private const val MODEL_PATH = "model_price.tflite"
    }
}