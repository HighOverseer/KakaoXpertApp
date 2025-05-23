package com.neotelemetrixgdscunand.kakaoxpert.data

import com.neotelemetrixgdscunand.kakaoxpert.data.remote.IoTDeviceService
import com.neotelemetrixgdscunand.kakaoxpert.data.utils.callApiFromNetwork
import com.neotelemetrixgdscunand.kakaoxpert.domain.common.DataError
import com.neotelemetrixgdscunand.kakaoxpert.domain.common.Result
import com.neotelemetrixgdscunand.kakaoxpert.domain.common.RootNetworkError
import com.neotelemetrixgdscunand.kakaoxpert.domain.data.IoTDeviceRepository
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.IoTDataOverview
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.IoTDevice
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.SensorItemData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IoTDeviceRepositoryImpl @Inject constructor(
    private val iotDeviceService: IoTDeviceService,
    private val dataMapper: DataMapper
) : IoTDeviceRepository {

    override suspend fun addIoTDeviceToAccount(
        deviceId: String,
        deviceKey: String
    ): Result<List<IoTDevice>, DataError.NetworkError> {
        return callApiFromNetwork {
            val response = iotDeviceService.addIoTDeviceToAccount(deviceId, deviceKey)
            val iotDevicesDto = response.data ?: return@callApiFromNetwork Result.Error(
                RootNetworkError.UNEXPECTED_ERROR
            )
            val iotDevices = iotDevicesDto.mapNotNull {
                dataMapper.mapIoTDeviceDtoToDomain(it)
            }

            Result.Success(iotDevices)
        }
    }

    override suspend fun getAllIoTData(): Result<List<SensorItemData>, DataError.NetworkError> {
        return callApiFromNetwork {
            val response = iotDeviceService.getAllIoTData()
            val ioTDataDto = response.data ?: return@callApiFromNetwork Result.Error(
                RootNetworkError.UNEXPECTED_ERROR
            )

            val sensorItemDatas = mutableListOf<SensorItemData>()
            ioTDataDto.forEach { dto ->
                dataMapper.mapIoTDataDtoToDomain(dto)?.also {
                    sensorItemDatas.addAll(
                        listOf(it.first, it.second, it.third)
                    )
                }
            }

            return@callApiFromNetwork Result.Success(sensorItemDatas)
        }
    }

    override suspend fun getAllConnectedIoTDevices(): Result<List<IoTDevice>, DataError.NetworkError> {
        return callApiFromNetwork {
            val response = iotDeviceService.getAllConnectedDevice()
            val iotDevicesDto = response.data ?: return@callApiFromNetwork Result.Error(
                RootNetworkError.UNEXPECTED_ERROR
            )
            val iotDevices = iotDevicesDto.mapNotNull {
                dataMapper.mapIoTDeviceDtoToDomain(it)
            }

            Result.Success(iotDevices)
        }
    }

    override suspend fun getIoTOverviewData(): Result<IoTDataOverview, DataError.NetworkError> {
        return callApiFromNetwork {
            val response = iotDeviceService.getIoTDataOverview()
            val ioTDataOverviewDto = response.data ?: return@callApiFromNetwork Result.Error(
                RootNetworkError.UNEXPECTED_ERROR
            )
            val ioTDataOverview = dataMapper.mapIoTDataOverviewDtoToDomain(ioTDataOverviewDto)

            Result.Success(ioTDataOverview)
        }
    }

    override suspend fun getIoTDataOfSelectedDevice(iotDeviceId: Int): Result<List<SensorItemData>, DataError.NetworkError> {
        return callApiFromNetwork {
            val response = iotDeviceService.getDataOfSelectedIoTDevice(iotDeviceId)
            val ioTDataDto = response.data ?: return@callApiFromNetwork Result.Error(
                RootNetworkError.UNEXPECTED_ERROR
            )

            val sensorItemDatas = mutableListOf<SensorItemData>()
            ioTDataDto.forEach { dto ->
                dataMapper.mapIoTDataDtoToDomain(dto)?.also {
                    sensorItemDatas.addAll(
                        listOf(it.first, it.second, it.third)
                    )
                }
            }

            return@callApiFromNetwork Result.Success(sensorItemDatas)
        }
    }

    override suspend fun deleteIoTDeviceFromAccount(deviceId: Int): Result<List<IoTDevice>, DataError.NetworkError> {
        return callApiFromNetwork {
            val response = iotDeviceService.deleteSelectedIoTDeviceIdFromAccount(deviceId)
            val iotDevicesDto = response.data ?: return@callApiFromNetwork Result.Error(
                RootNetworkError.UNEXPECTED_ERROR
            )
            val iotDevices = iotDevicesDto.mapNotNull {
                dataMapper.mapIoTDeviceDtoToDomain(it)
            }

            Result.Success(iotDevices)
        }
    }

    override suspend fun resetSensorDataOfSelectedIoTDevice(deviceId: Int): Result<IoTDataOverview, DataError.NetworkError> {
        return callApiFromNetwork {
            val response = iotDeviceService.resetSensorDataOfSelectedIoTDeviceId(deviceId)
            val ioTDataOverviewDto = response.data ?: return@callApiFromNetwork Result.Error(
                RootNetworkError.UNEXPECTED_ERROR
            )
            val ioTDataOverview = dataMapper.mapIoTDataOverviewDtoToDomain(ioTDataOverviewDto)

            Result.Success(ioTDataOverview)
        }
    }
}