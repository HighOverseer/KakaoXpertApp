package com.neotelemetrixgdscunand.kakaoxpert.domain.data

import com.neotelemetrixgdscunand.kakaoxpert.domain.common.DataError
import com.neotelemetrixgdscunand.kakaoxpert.domain.common.Result
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.IoTDataOverview
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.IoTDevice
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.SensorItemData

interface IoTDeviceRepository {
    suspend fun addIoTDeviceToAccount(
        deviceId: String,
        deviceKey: String
    ): Result<List<IoTDevice>, DataError.NetworkError>

    suspend fun getIoTOverviewData(): Result<IoTDataOverview, DataError.NetworkError>

    suspend fun getAllConnectedIoTDevices(): Result<List<IoTDevice>, DataError.NetworkError>

    suspend fun getAllIoTData(): Result<List<SensorItemData>, DataError.NetworkError>

    suspend fun getIoTDataOfSelectedDevice(iotDeviceId: Int): Result<List<SensorItemData>, DataError.NetworkError>

    suspend fun deleteIoTDeviceFromAccount(
        deviceId: Int,
    ): Result<List<IoTDevice>, DataError.NetworkError>
}