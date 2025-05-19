package com.neotelemetrixgdscunand.kakaoxpert

import android.app.Application
import androidx.work.Configuration
import com.neotelemetrixgdscunand.kakaoxpert.presentation.worker.CommonWorkerFactory
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class KakaoXpertApplication : Application(), Configuration.Provider {
    @Inject
    lateinit var workerFactory: CommonWorkerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
}
