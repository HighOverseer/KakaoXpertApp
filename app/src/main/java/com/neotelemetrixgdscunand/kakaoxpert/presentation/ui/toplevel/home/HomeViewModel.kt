package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.toplevel.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neotelemetrixgdscunand.kakaoxpert.domain.data.Repository
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.DiagnosisSessionPreview
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    val diagnosisHistory: StateFlow<ImmutableList<DiagnosisSessionPreview>> =
        repository.getAllSavedDiagnosisSessionPreviews()
            .map {
                it.toImmutableList()
            }
            .flowOn(Dispatchers.Default)
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                persistentListOf()
            )

}