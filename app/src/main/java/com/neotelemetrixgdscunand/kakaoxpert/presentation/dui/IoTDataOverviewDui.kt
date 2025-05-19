package com.neotelemetrixgdscunand.kakaoxpert.presentation.dui

import com.neotelemetrixgdscunand.kakaoxpert.presentation.utils.UIText

data class IoTDataOverviewDui(
    val averageTemperatureValue:UIText = UIText.DynamicString(""),
    val averageHumidityValue:UIText = UIText.DynamicString(""),
    val averageLightIntensityValue:UIText = UIText.DynamicString(""),
)