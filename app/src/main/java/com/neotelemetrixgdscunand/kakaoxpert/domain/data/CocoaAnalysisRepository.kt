package com.neotelemetrixgdscunand.kakaoxpert.domain.data

import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.neotelemetrixgdscunand.kakaoxpert.domain.common.DataError
import com.neotelemetrixgdscunand.kakaoxpert.domain.common.Result
import com.neotelemetrixgdscunand.kakaoxpert.domain.common.RootNetworkError
import com.neotelemetrixgdscunand.kakaoxpert.domain.common.SyncError
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.AnalysisSession
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.AnalysisSessionPreview
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.DetectedCocoa
import kotlinx.coroutines.flow.Flow

interface CocoaAnalysisRepository {
    suspend fun saveDiagnosis(
        sessionName: String,
        imageOrUrlPath: String,
        predictedPrice: Float,
        detectedCocoas: List<DetectedCocoa>
    ): Int

    fun getAllSessionPreviews(query:String = ""): Flow<PagingData<AnalysisSessionPreview>>

    fun getSomeSessionPreviews(): Flow<List<AnalysisSessionPreview>>

    suspend fun getDiagnosisSession(sessionId: Int): Result<AnalysisSession, DataError.NetworkError>

    suspend fun syncAllSessionPreviewsFromRemote(): Result<Unit, DataError>

    suspend fun syncAllUnsavedSessionsFromLocal(): Result<Unit, DataError>

    suspend fun syncAllSavedSessionsFromRemote(): Result<Unit, DataError>

    suspend fun resetAllLocalData()

    companion object {
        const val PAGE_SIZE = 10
        const val ENABLE_PLACE_HOLDERS = true
    }
}