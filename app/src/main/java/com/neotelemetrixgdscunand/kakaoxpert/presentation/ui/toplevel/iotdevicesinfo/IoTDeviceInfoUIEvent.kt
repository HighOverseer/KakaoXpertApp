package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.toplevel.iotdevicesinfo

import com.neotelemetrixgdscunand.kakaoxpert.presentation.utils.UIText

sealed class IoTDeviceInfoUIEvent {
    data object OnNoDeviceFound : IoTDeviceInfoUIEvent()
    data object OnAddDeviceFormIsNotValid : IoTDeviceInfoUIEvent()
    data class OnFailedAddingDeviceIoT(val errorUIText: UIText) : IoTDeviceInfoUIEvent()
    data object OnSuccessAddingDeviceIoT : IoTDeviceInfoUIEvent()
    data class OnFailedGetIoTDataOverview(val errorUIText: UIText) : IoTDeviceInfoUIEvent()
}