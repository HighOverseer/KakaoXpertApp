package com.neotelemetrixgdscunand.kakaoxpert.presentation.worker

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.neotelemetrixgdscunand.kakaoxpert.domain.usecase.SyncCocoaAnalysisDataUseCase
import javax.inject.Inject

class CommonWorkerFactory @Inject constructor(
    private val syncCocoaAnalysisDataUseCase: SyncCocoaAnalysisDataUseCase
) : WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        return when (workerClassName) {
            CocoaAnalysisSyncWorker::class.java.name -> CocoaAnalysisSyncWorker(
                appContext,
                workerParameters,
                syncCocoaAnalysisDataUseCase
            )
            //other could come here, or directly return single one
            else -> null
        }
    }
}