package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.toplevel.diagnosishistory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.neotelemetrixgdscunand.kakaoxpert.domain.data.CocoaAnalysisRepository
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.SearchAnalysisHistoryCategory
import com.neotelemetrixgdscunand.kakaoxpert.presentation.mapper.DuiMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class DiagnosisViewModel @Inject constructor(
    private val cocoaAnalysisRepository: CocoaAnalysisRepository,
    private val duiMapper: DuiMapper
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _selectedSearchCategory = MutableStateFlow(SearchAnalysisHistoryCategory.ALL)
    val selectedSearchCategory = _selectedSearchCategory.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val diagnosisHistoryPreview = _searchQuery.combine(selectedSearchCategory){ query, selectedSearchCategory ->
        Pair(query, selectedSearchCategory)
    }.flatMapLatest { (searchQuery, selectedSearchCategory) ->
        cocoaAnalysisRepository.getAllSessionPreviews(searchQuery, selectedSearchCategory)
            .map { pagingData ->
                pagingData.map {
                    duiMapper.mapDiagnosisSessionPreviewToDui(it)
                }
            }
    }.flowOn(Dispatchers.Default)
        .cachedIn(viewModelScope)

    fun onSearchBarQueryChange(query: String) {
        _searchQuery.update { query }
    }

    fun onSelectedCategoryChanged(category: SearchAnalysisHistoryCategory) {
        _selectedSearchCategory.update { category }
    }

//    fun search(query: String) {
//        job?.cancel()
//        job = viewModelScope.launch {
//            withContext(Dispatchers.Default) {
//                diagnosisHistoryPreview.value.filter {
//                    it.title.contains("query", ignoreCase = true)
//                }
//            }
//        }
//    }
}