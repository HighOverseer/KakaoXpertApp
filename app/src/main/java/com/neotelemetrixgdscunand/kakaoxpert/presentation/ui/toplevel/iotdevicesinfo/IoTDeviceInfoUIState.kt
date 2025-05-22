package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.toplevel.iotdevicesinfo


import com.neotelemetrixgdscunand.kakaoxpert.domain.model.IoTDevice
import com.neotelemetrixgdscunand.kakaoxpert.presentation.dui.IoTDataOverviewDui
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class IoTDeviceInfoUIState(
    val connectedDevices: ImmutableList<IoTDevice> = persistentListOf(),
    val isAddDeviceDialogShown: Boolean = false,
    val selectedDeviceForDetailDialog: IoTDevice? = null,
    val ioTDataOverviewDui: IoTDataOverviewDui = IoTDataOverviewDui()
)