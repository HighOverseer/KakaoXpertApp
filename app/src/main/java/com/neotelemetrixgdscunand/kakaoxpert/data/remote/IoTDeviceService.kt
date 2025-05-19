package com.neotelemetrixgdscunand.kakaoxpert.data.remote

import com.neotelemetrixgdscunand.kakaoxpert.data.remote.dto.IoTDataDto
import com.neotelemetrixgdscunand.kakaoxpert.data.remote.dto.IoTDataOverviewDto
import com.neotelemetrixgdscunand.kakaoxpert.data.remote.dto.IoTDeviceDto
import com.neotelemetrixgdscunand.kakaoxpert.data.remote.dto.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface IoTDeviceService {

    @POST("user-iot-device")
    @FormUrlEncoded
    suspend fun addIoTDeviceToAccount(
        @Field("iot_device_id") deviceId: String,
        @Field("key") deviceKey: String,
    ): Response<List<IoTDeviceDto>>

    @GET("user-iot-device")
    suspend fun getAllConnectedDevice(): Response<List<IoTDeviceDto>>

    @GET("sensor-data-overview")
    suspend fun getIoTDataOverview(): Response<IoTDataOverviewDto>

    @GET("sensor-data")
    suspend fun getAllIoTData(): Response<List<IoTDataDto>>
}