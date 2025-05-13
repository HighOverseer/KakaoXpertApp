package com.neotelemetrixgdscunand.kakaoxpert.domain.model


enum class CocoaDisease(val id: Int) {
    NONE(2),
    HELOPELTIS(3),
    BLACKPOD(1),
    POD_BORER(4); //PBK

    companion object {

        fun getDiseaseFromId(
            diseaseId: Int
        ): CocoaDisease? {
            return when (diseaseId) {
                1 -> BLACKPOD
                2 -> NONE
                3 -> HELOPELTIS
                4 -> POD_BORER
                else -> null
            }
        }

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

