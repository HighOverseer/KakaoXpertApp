package com.neotelemetrixgdscunand.kakaoxpert.data.remote

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File


fun String.toTextRequestBody():RequestBody{
    return this.toRequestBody(MultipartBody.FORM)
}

fun List<String>.toTextRequestBody():RequestBody{
    return this
        .joinToString(",")
        .toTextRequestBody()
}

fun File.asImageRequestBody():RequestBody{
    return this.asRequestBody("image/jpeg".toMediaTypeOrNull())
}