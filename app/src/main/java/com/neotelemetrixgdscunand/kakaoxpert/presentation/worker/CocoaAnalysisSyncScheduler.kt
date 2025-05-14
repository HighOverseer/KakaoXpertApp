package com.neotelemetrixgdscunand.kakaoxpert.presentation.worker

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.neotelemetrixgdscunand.kakaoxpert.domain.data.DataPreference
import java.util.concurrent.TimeUnit

object CocoaAnalysisSyncScheduler {

    private const val WORK_NAME = "periodic_cocoa_analysis_sync"
    private const val FALLBACK_WORK_NAME = "fallback_cocoa_analysis_sync"

    fun startPeriodicSync(context: Context){
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val request = PeriodicWorkRequestBuilder<CocoaAnalysisSyncWorker>(
            DataPreference.SYNC_TIME_PERIOD_IN_MILLIS, TimeUnit.MILLISECONDS
        ).setConstraints(constraints)
            .setBackoffCriteria(
                BackoffPolicy.LINEAR,
                DataPreference.SYNC_BACK_OFF_DELAY,
                TimeUnit.MILLISECONDS
            )
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )

        val fallbackWorkRequest = OneTimeWorkRequestBuilder<CocoaAnalysisSyncWorker>()
            .setInitialDelay(DataPreference.FALLBACK_ONE_TIME_REQUEST_DELAY, TimeUnit.MILLISECONDS)
            .setConstraints(
                Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
            )
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            FALLBACK_WORK_NAME,
            ExistingWorkPolicy.REPLACE,
            fallbackWorkRequest
        )
    }

    fun stopPeriodicSync(context: Context){
        WorkManager.getInstance(context).cancelUniqueWork(WORK_NAME)
        WorkManager.getInstance(context).cancelUniqueWork(FALLBACK_WORK_NAME)
    }
}