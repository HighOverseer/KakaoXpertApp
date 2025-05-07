package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.cacaoimagedetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.Navigation
import com.neotelemetrixgdscunand.kamekapp.domain.data.Repository
import com.neotelemetrixgdscunand.kamekapp.domain.model.BoundingBox
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CacaoImageDetailViewModel @Inject constructor(
    val repository: Repository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val imagePath: String
    val boundingBoxes: List<BoundingBox>

    private val _invalidSessionEvent = Channel<Unit>()
    val invalidSession = _invalidSessionEvent.receiveAsFlow()

    init {
        val extras = savedStateHandle.toRoute<Navigation.CacaoImageDetail>()
        val selectedDiagnosisSession = repository.getDiagnosisSession(extras.diagnosisSessionId)

        imagePath = selectedDiagnosisSession.imageUrlOrPath
        val boundingBoxes = mutableListOf<BoundingBox>()

        if (extras.detectedCacaoId == null) {
            boundingBoxes.addAll(
                selectedDiagnosisSession.detectedCacaos.map {
                    it.boundingBox
                }
            )
        } else {
            selectedDiagnosisSession
                .detectedCacaos
                .find { it.id == extras.detectedCacaoId }
                ?.boundingBox?.let {
                    boundingBoxes.add(it)
                } ?: viewModelScope.launch {
                _invalidSessionEvent.send(Unit)
            }
        }

        this.boundingBoxes = boundingBoxes
    }
}