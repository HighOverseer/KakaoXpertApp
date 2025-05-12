package com.neotelemetrixgdscunand.kakaoxpert.domain.model


enum class CocoaDisease {
    NONE,
    HELOPELTIS,
    BLACKPOD,
    POD_BORER; //PBK

    companion object {
        fun getDiseaseFromName(
            name: String
        ): CocoaDisease {
            return when (name.lowercase().trim()) {
                "healthy", "none" -> NONE
                "blackpod" -> BLACKPOD
                "mirid", "helopeltis" -> HELOPELTIS
                "pbk" -> POD_BORER
                else -> NONE
            }
        }
    }
}

