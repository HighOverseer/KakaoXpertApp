package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.sensordatadetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neotelemetrixgdscunand.kakaoxpert.domain.common.Result
import com.neotelemetrixgdscunand.kakaoxpert.domain.data.IoTDeviceRepository
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.SensorItemData
import com.neotelemetrixgdscunand.kakaoxpert.presentation.utils.toErrorUIText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SensorDataDetailViewModel @Inject constructor(
    private val ioTDeviceRepository: IoTDeviceRepository
) : ViewModel() {

    private val _uiEvent = Channel<SensorDataDetailUIEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _temperatureSensorData =
        MutableStateFlow<ImmutableList<SensorItemData.Temperature>>(persistentListOf())
    val temperatureSensorData = _temperatureSensorData.asStateFlow()

    private val _humiditySensorData =
        MutableStateFlow<ImmutableList<SensorItemData.Humidity>>(persistentListOf())
    val humiditySensorData = _humiditySensorData.asStateFlow()

    private val _lightIntensitySensorData =
        MutableStateFlow<ImmutableList<SensorItemData.LightIntensity>>(persistentListOf())
    val lightIntensitySensorData = _lightIntensitySensorData.asStateFlow()


    init {
        getAllSensorData()
    }

    private fun getAllSensorData() {
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = ioTDeviceRepository.getAllIoTData()) {
                is Result.Error -> {
                    val errorUIText = result.toErrorUIText()
                    _uiEvent.send(
                        SensorDataDetailUIEvent.OnFailedGettingSensorData(errorUIText)
                    )
                }

                is Result.Success -> {
                    val sensorsData = result.data
                    withContext(Dispatchers.Default) {
                        val temperatureSensorData =
                            sensorsData.filterIsInstance<SensorItemData.Temperature>()
                        val humiditySensorData =
                            sensorsData.filterIsInstance<SensorItemData.Humidity>()
                        val lightIntensitySensorData =
                            sensorsData.filterIsInstance<SensorItemData.LightIntensity>()

                        _temperatureSensorData.update { temperatureSensorData.toImmutableList() }
                        _humiditySensorData.update { humiditySensorData.toImmutableList() }
                        _lightIntensitySensorData.update { lightIntensitySensorData.toImmutableList() }
                    }
                }
            }
        }
    }
}