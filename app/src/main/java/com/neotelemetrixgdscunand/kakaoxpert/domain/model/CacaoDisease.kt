package com.neotelemetrixgdscunand.kakaoxpert.domain.model

import com.neotelemetrixgdscunand.kakaoxpert.R

enum class CacaoDisease(
    val nameResId: Int,
    val causeStringResId: Int,
    val symptomStringResId: Int,
    val seedConditionStringResId: Int,
    val solutionStringResId: Int,
    val controlStringResId: List<Int>
) {
    NONE(
        nameResId = R.string.name_none,
        causeStringResId = R.string.strip,
        symptomStringResId = R.string.symptom_none,
        seedConditionStringResId = R.string.seedcond_none,
        solutionStringResId = R.string.strip,
        controlStringResId = listOf(
            R.string.control_none1,
            R.string.control_none2,
            R.string.control_none3,
            R.string.control_none4
        )
    ),
    HELOPELTIS(
        nameResId = R.string.name_helo,
        causeStringResId = R.string.cause_helopelthis,
        symptomStringResId = R.string.symptom_helopelthis,
        seedConditionStringResId = R.string.seedcond_helo,
        solutionStringResId = R.string.disease_solution_temp,
        controlStringResId = listOf(
            R.string.control_helo1,
            R.string.control_helo2,
            R.string.control_helo3
        )
    ),
    BLACKPOD(
        nameResId = R.string.name_blackpod,
        causeStringResId = R.string.cause_blackpod,
        symptomStringResId = R.string.symptom_blackpod,
        seedConditionStringResId = R.string.seedcond_blackpod,
        solutionStringResId = R.string.disease_solution_temp,
        controlStringResId = listOf(
            R.string.control_blackpod1,
            R.string.control_blackpod2,
            R.string.control_blackpod3
        )
    );

    companion object {
        fun getDiseaseFromName(
            name: String
        ): CacaoDisease? {
            return when (name.lowercase().trim()) {
                "healthy", "none" -> NONE
                "blackpod" -> BLACKPOD
                "mirid", "helopeltis" -> HELOPELTIS
                else -> null
            }
        }
    }
}

