package com.neotelemetrixgdscunand.kakaoxpert.domain.presentation


sealed interface CocoaDamageLevelPredictionResult {
    data class Success(
        val damageLevels: List<Float>
    ) : CocoaDamageLevelPredictionResult

    data class Error(
        val exception: Exception
    ) : CocoaDamageLevelPredictionResult
}