package com.neotelemetrixgdscunand.kamekapp.domain.data

import com.neotelemetrixgdscunand.kamekapp.domain.model.DiagnosisSession
import com.neotelemetrixgdscunand.kamekapp.domain.model.DiagnosisSessionPreview
import kotlinx.coroutines.flow.Flow

interface Repository {
    fun saveDiagnosis(newDiagnosisSession: DiagnosisSession)

    fun getAllSavedDiagnosisSessions(): Flow<List<DiagnosisSession>>

    fun getDiagnosisSession(id: Int): DiagnosisSession

    fun getAllSavedDiagnosisSessionPreviews(): Flow<List<DiagnosisSessionPreview>>
}