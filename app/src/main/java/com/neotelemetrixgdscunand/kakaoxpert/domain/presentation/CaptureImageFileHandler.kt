package com.neotelemetrixgdscunand.kakaoxpert.domain.presentation

interface CaptureImageFileHandler {
    suspend fun saveImage(
        imageUriPath: String?,
        imageBytes: ByteArray?,
        fileName: String
    ): String?

    fun deleteImageFile(
        fileUriPath: String
    )
}