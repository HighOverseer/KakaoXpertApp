package com.neotelemetrixgdscunand.kakaoxpert.domain.model

sealed interface SensorItemData {
    val unit: String
    val value: Float
    val timeString: String

    data class Temperature(
        override val value: Float,
        override val timeString: String
    ) : SensorItemData {
        override val unit: String = "°C"
    }

    data class Humidity(
        override val value: Float,
        override val timeString: String
    ) : SensorItemData {
        override val unit: String = "%"
    }

    data class LightIntensity(
        override val value: Float,
        override val timeString: String
    ) : SensorItemData {
        override val unit: String = " Lux"
    }
}