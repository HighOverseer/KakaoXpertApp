package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.cacaoimagedetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.Navigation
import com.neotelemetrixgdscunand.kakaoxpert.domain.data.CocoaAnalysisRepository
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.BoundingBox
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CacaoImageDetailViewModel @Inject constructor(
    val cocoaAnalysisRepository: CocoaAnalysisRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(CocoaImageDetailUIState())
    val uiState = _uiState.asStateFlow()

    private val _invalidSessionEvent = Channel<Unit>()
    val invalidSession = _invalidSessionEvent.receiveAsFlow()

    init {
        viewModelScope.launch {
            val extras = savedStateHandle.toRoute<Navigation.CacaoImageDetail>()
            val selectedDiagnosisSession = cocoaAnalysisRepository.getDiagnosisSession(extras.diagnosisSessionId)

            val boundingBoxes = mutableListOf<BoundingBox>()

            if (extras.detectedCacaoId == null) {
                boundingBoxes.addAll(
                    selectedDiagnosisSession.detectedCocoas.map {
                        it.boundingBox
                    }
                )
            } else {
                selectedDiagnosisSession
                    .detectedCocoas
                    .find { it.id == extras.detectedCacaoId }
                    ?.boundingBox?.let {
                        boundingBoxes.add(it)
                    } ?: viewModelScope.launch {
                    _invalidSessionEvent.send(Unit)
                }
            }

            _uiState.update {
                CocoaImageDetailUIState(
                    imagePath = selectedDiagnosisSession.imageUrlOrPath,
                    boundingBox = boundingBoxes.toImmutableList()
                )
            }
        }
    }
}