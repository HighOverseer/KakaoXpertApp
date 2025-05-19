package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.sensordatadetails

import com.neotelemetrixgdscunand.kakaoxpert.presentation.utils.UIText

sealed interface SensorDataDetailUIEvent {
    data class OnFailedGettingSensorData(val errorUIText: UIText) : SensorDataDetailUIEvent
}