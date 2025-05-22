package com.neotelemetrixgdscunand.kakaoxpert.data.remote.dto

import com.google.gson.annotations.SerializedName

data class IoTDeviceDto(

    @field:SerializedName("iot_device_id")
    val iotDeviceId: Int? = null,

    @field:SerializedName("is_active")
    val isActive:Boolean? = false
)
