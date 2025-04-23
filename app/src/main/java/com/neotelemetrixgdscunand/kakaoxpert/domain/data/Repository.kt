package com.neotelemetrixgdscunand.kakaoxpert.domain.data

import com.neotelemetrixgdscunand.kakaoxpert.domain.model.DiagnosisSession
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.DiagnosisSessionPreview
import kotlinx.coroutines.flow.Flow

interface Repository {
    fun saveDiagnosis(newDiagnosisSession: DiagnosisSession)

    fun getAllSavedDiagnosisSessions(): Flow<List<DiagnosisSession>>

    fun getDiagnosisSession(id: Int): DiagnosisSession

    fun getAllSavedDiagnosisSessionPreviews(): Flow<List<DiagnosisSessionPreview>>
}