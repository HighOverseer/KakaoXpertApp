package com.neotelemetrixgdscunand.kakaoxpert

import android.app.Application
import androidx.work.Configuration
import com.neotelemetrixgdscunand.kakaoxpert.domain.common.CocoaAnalysisSyncType
import com.neotelemetrixgdscunand.kakaoxpert.domain.common.Result
import com.neotelemetrixgdscunand.kakaoxpert.domain.common.SyncSuccess
import com.neotelemetrixgdscunand.kakaoxpert.domain.usecase.SyncCocoaAnalysisDataUseCase
import com.neotelemetrixgdscunand.kakaoxpert.presentation.worker.CommonWorkerFactory
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class KakaoXpertApplication : Application(), Configuration.Provider {
    @Inject
    lateinit var workerFactory: CommonWorkerFactory

    @Inject
    lateinit var syncCocoaAnalysisDataUseCase: SyncCocoaAnalysisDataUseCase

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    override fun onCreate() {
        super.onCreate()
        syncAnyCocoaAnalysisTypeThatNeedTo()
    }

    private fun syncAnyCocoaAnalysisTypeThatNeedTo() {
        applicationScope.launch(Dispatchers.IO) {
            var syncRequiredSuccess = 0

            for (syncType in CocoaAnalysisSyncType.entries) {
                if(syncRequiredSuccess > 1) break

                val result = syncCocoaAnalysisDataUseCase(syncType)

                if(result is Result.Success && result.data == SyncSuccess.NORMAL){
                    syncRequiredSuccess += 1
                    continue
                }

                if (result is Result.Success && result.data == SyncSuccess.ALREADY_SYNCED_OR_IN_SYNCING) {
                    continue
                }



                break
            }
        }

    }

}
