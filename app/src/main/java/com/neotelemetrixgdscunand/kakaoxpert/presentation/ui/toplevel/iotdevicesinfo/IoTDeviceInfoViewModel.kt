package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.toplevel.iotdevicesinfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neotelemetrixgdscunand.kakaoxpert.R
import com.neotelemetrixgdscunand.kakaoxpert.domain.common.Result
import com.neotelemetrixgdscunand.kakaoxpert.domain.common.RootNetworkError
import com.neotelemetrixgdscunand.kakaoxpert.domain.data.IoTDeviceRepository
import com.neotelemetrixgdscunand.kakaoxpert.presentation.mapper.DuiMapper
import com.neotelemetrixgdscunand.kakaoxpert.presentation.utils.UIText
import com.neotelemetrixgdscunand.kakaoxpert.presentation.utils.toErrorUIText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IoTDeviceInfoViewModel @Inject constructor(
    private val iotDeviceRepository: IoTDeviceRepository,
    private val duiMapper: DuiMapper
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _event = Channel<IoTDeviceInfoUIEvent>()
    val event = _event.receiveAsFlow()

    private val _uiState = MutableStateFlow(IoTDeviceInfoUIState())
    val uiState = _uiState.asStateFlow()

    private var addJob: Job? = null

    init {
        getIoTDataOverview()
        getAllConnectedIoTDevices()
    }

    fun showAddIoTDeviceDialog() {
        _uiState.update {
            it.copy(isAddDeviceDialogShown = true)
        }
    }

    fun dismissAddIoTDeviceDialog() {
        _uiState.update {
            it.copy(isAddDeviceDialogShown = false)
        }
    }

    private fun getIoTDataOverview() {
        viewModelScope.launch {
            when (val result = iotDeviceRepository.getIoTOverviewData()) {
                is Result.Error -> {
                    val errorUIText = result.toErrorUIText()
                    _event.send(
                        IoTDeviceInfoUIEvent.OnFailedGetIoTDataOverview(errorUIText)
                    )
                }

                is Result.Success -> {
                    val ioTDataOverview = result.data
                    _uiState.update {
                        it.copy(
                            ioTDataOverviewDui = DuiMapper.mapIoTDataOverviewToDui(ioTDataOverview)
                        )
                    }
                }
            }
        }
    }

    private fun getAllConnectedIoTDevices() {
        viewModelScope.launch {
            when (val result = iotDeviceRepository.getAllConnectedIoTDevices()) {
                is Result.Error -> {
                    val errorUIText = when (result.error) {
                        RootNetworkError.NOT_FOUND -> return@launch
                        else -> result.toErrorUIText()
                    }
                    _event.send(
                        IoTDeviceInfoUIEvent.OnFailedAddingDeviceIoT(errorUIText)
                    )
                }

                is Result.Success -> {
                    val iotDevices = result.data
                    _uiState.update {
                        it.copy(
                            connectedDevices = iotDevices.toImmutableList()
                        )
                    }
                }
            }
        }
    }

    fun addIoTDeviceToAccount(
        deviceId: String,
        deviceKey: String
    ) {
        if (addJob?.isCompleted == false) return

        addJob = viewModelScope.launch {
            _isLoading.update { true }

            if (deviceId.isBlank() || deviceKey.isBlank()) {
                _event.send(
                    IoTDeviceInfoUIEvent.OnAddDeviceFormIsNotValid
                )
                return@launch
            }

            when (val result = iotDeviceRepository.addIoTDeviceToAccount(deviceId, deviceKey)) {
                is Result.Error -> {
                    val errorUIText = when (result.error) {
                        RootNetworkError.NOT_FOUND -> UIText.StringResource(R.string.no_device_found)
                        RootNetworkError.RESOURCE_CONFLICT -> UIText.StringResource(R.string.perangkat_sudah_ditambahkan)
                        else -> result.toErrorUIText()
                    }
                    _event.send(
                        IoTDeviceInfoUIEvent.OnFailedAddingDeviceIoT(errorUIText)
                    )
                }

                is Result.Success -> {
                    val iotDevices = result.data
                    _uiState.update {
                        it.copy(
                            connectedDevices = iotDevices.toImmutableList()
                        )
                    }
                    _event.send(
                        IoTDeviceInfoUIEvent.OnSuccessAddingDeviceIoT
                    )
                }
            }


        }.also { job ->
            job.invokeOnCompletion {
                _isLoading.update { false }
                _uiState.update { it.copy(isAddDeviceDialogShown = false) }
            }
        }
    }
}