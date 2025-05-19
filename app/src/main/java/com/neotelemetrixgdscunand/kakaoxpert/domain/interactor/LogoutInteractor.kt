package com.neotelemetrixgdscunand.kakaoxpert.domain.interactor

import com.neotelemetrixgdscunand.kakaoxpert.domain.data.AuthPreference
import com.neotelemetrixgdscunand.kakaoxpert.domain.data.AuthRepository
import com.neotelemetrixgdscunand.kakaoxpert.domain.data.CocoaAnalysisRepository
import com.neotelemetrixgdscunand.kakaoxpert.domain.data.DataPreference
import com.neotelemetrixgdscunand.kakaoxpert.domain.usecase.LogoutUseCase
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.withContext

class LogoutInteractor(
    private val authPreference: AuthPreference,
    private val dataPreference: DataPreference,
    private val cocoaAnalysisRepository: CocoaAnalysisRepository
):LogoutUseCase {
    override suspend fun invoke() {
        withContext(NonCancellable){
            authPreference.clearToken()
            dataPreference.resetAll()
            cocoaAnalysisRepository.resetAllLocalData()
        }
    }
}