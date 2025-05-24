package com.neotelemetrixgdscunand.kakaoxpert.domain.model


enum class DamageLevelCategory {
    LOW,
    MEDIUM,
    HIGH;


    companion object {
        fun getFromDamageLevel(damageLevel: Float): DamageLevelCategory {
            return when {
                damageLevel <= 30 -> LOW
                damageLevel > 30 && damageLevel <= 60 -> MEDIUM
                else -> HIGH
            }
        }
    }
}




