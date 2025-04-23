package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.takephoto.util

import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import androidx.core.net.toFile
import androidx.core.net.toUri
import com.neotelemetrixgdscunand.kakaoxpert.domain.presentation.CaptureImageFileHandler
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CapturedImageFileHandlerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : CaptureImageFileHandler {

    override suspend fun saveImage(
        imageUriPath: String?,
        imageBytes: ByteArray?,
        fileName: String
    ): String? {
        return withContext(Dispatchers.IO) {
            if (imageBytes == null || imageUriPath == null) return@withContext null

            val imageUri = imageUriPath.toUri()
            val mimeType = context.contentResolver.getType(imageUri)
            val extension = MimeTypeMap
                .getSingleton()
                .getExtensionFromMimeType(mimeType) ?: "jpeg"

            if (imageUri.scheme == "file") {
                val oldFile = imageUri.toFile()
                if (oldFile.exists()) oldFile.delete()
            }

            val file = createCustomTempFile(context, extension = extension)
            file.writeBytes(imageBytes)
            val fullFileName = "$fileName.$extension"
            val renameFileUri = renameFile(file, fullFileName)

            return@withContext renameFileUri.path
        }
    }

    override fun deleteImageFile(fileUriPath: String) {
        val fileUri = fileUriPath.toUri()

        if (fileUri.scheme == "file") {
            val file = fileUri.toFile()
            if (file.exists()) file.delete()
        }
    }

    private fun renameFile(file: File?, newName: String): Uri {
        if (file == null) throw Exception("File is null")

        if (!file.exists()) throw Exception("File not found")

        val newFile = File(file.parent, newName)

        file.renameTo(newFile)
        return newFile.toUri()
    }


}