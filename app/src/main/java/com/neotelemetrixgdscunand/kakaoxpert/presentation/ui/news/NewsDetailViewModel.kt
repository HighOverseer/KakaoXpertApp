package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.news

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.neotelemetrixgdscunand.kakaoxpert.domain.common.Result
import com.neotelemetrixgdscunand.kakaoxpert.domain.data.NewsRepository
import com.neotelemetrixgdscunand.kakaoxpert.presentation.mapper.DuiMapper
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.Navigation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class NewsDetailViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
    private val duiMapper: DuiMapper,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val newsId = savedStateHandle.toRoute<Navigation.NewsDetail>().newsId
    private val newsType = savedStateHandle.toRoute<Navigation.NewsDetail>().newsType

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _onFailedGettingSelectedNewsDetailsEvent = Channel<Unit>()
    val onFailedGettingSelectedNewsDetailsEvent =
        _onFailedGettingSelectedNewsDetailsEvent.receiveAsFlow()

    val newsDetails = flow {
        _isLoading.update { true }

        when (val result = newsRepository.getNewsById(newsId, newsType = newsType)) {
            is Result.Error -> {
                _onFailedGettingSelectedNewsDetailsEvent.send(Unit)
            }

            is Result.Success -> {
                val newsDetailsDui = duiMapper.mapNewsDetailsToDui(result.data)
                emit(newsDetailsDui)
            }
        }
    }.onCompletion { _isLoading.update { false } }
        .flowOn(Dispatchers.IO)
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            null
        )
}