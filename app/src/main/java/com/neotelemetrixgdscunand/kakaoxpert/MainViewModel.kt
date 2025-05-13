package com.neotelemetrixgdscunand.kakaoxpert

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neotelemetrixgdscunand.kakaoxpert.domain.data.CocoaAnalysisRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val cocoaAnalysisRepository: CocoaAnalysisRepository
):ViewModel() {

    init {
        viewModelScope.launch {
            cocoaAnalysisRepository.syncAllSessionsFromRemote()
        }
    }
}