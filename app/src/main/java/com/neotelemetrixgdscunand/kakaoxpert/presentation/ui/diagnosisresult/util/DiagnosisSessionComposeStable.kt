package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.diagnosisresult.util

import com.neotelemetrixgdscunand.kamekapp.domain.model.DetectedCacao
import com.neotelemetrixgdscunand.kamekapp.domain.model.DiagnosisSession
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlin.random.Random

data class DiagnosisSessionComposeStable(
    val id: Int = Random.nextInt(0, 1_000_000),
    val title: String = "",
    val imageUrlOrPath: String = "",
    val date: String = "",
    val predictedPrice: Float = 0f,
    val detectedCacaos: ImmutableList<DetectedCacao> = persistentListOf()
) {
    companion object {
        fun getFromDomainModel(diagnosisSession: DiagnosisSession): DiagnosisSessionComposeStable {
            return DiagnosisSessionComposeStable(
                id = diagnosisSession.id,
                title = diagnosisSession.title,
                imageUrlOrPath = diagnosisSession.imageUrlOrPath,
                date = diagnosisSession.date,
                predictedPrice = diagnosisSession.predictedPrice,
                detectedCacaos = diagnosisSession.detectedCacaos.toImmutableList()
            )
        }
    }
}