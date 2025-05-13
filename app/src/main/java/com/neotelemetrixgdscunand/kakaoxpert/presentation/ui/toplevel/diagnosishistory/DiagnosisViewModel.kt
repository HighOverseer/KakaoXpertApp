package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.toplevel.diagnosishistory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neotelemetrixgdscunand.kakaoxpert.domain.data.CocoaAnalysisRepository
import com.neotelemetrixgdscunand.kakaoxpert.presentation.dui.AnalysisSessionPreviewDui
import com.neotelemetrixgdscunand.kakaoxpert.presentation.mapper.DuiMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DiagnosisViewModel @Inject constructor(
    private val cocoaAnalysisRepository: CocoaAnalysisRepository,
    private val duiMapper: DuiMapper
) : ViewModel() {

    val diagnosisHistoryPreview: StateFlow<ImmutableList<AnalysisSessionPreviewDui>> =
        cocoaAnalysisRepository.getAllSessionPreviews()
            .map {
                it.map {
                    DuiMapper.mapDiagnosisSessionPreviewToDui(it)
                }.toImmutableList()
            }
            .flowOn(Dispatchers.Default)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), persistentListOf())

    var job: Job? = null

    fun search(query: String) {
        job?.cancel()
        job = viewModelScope.launch {
            withContext(Dispatchers.Default) {
                diagnosisHistoryPreview.value.filter {
                    it.title.contains("query", ignoreCase = true)
                }
            }
        }
    }
}