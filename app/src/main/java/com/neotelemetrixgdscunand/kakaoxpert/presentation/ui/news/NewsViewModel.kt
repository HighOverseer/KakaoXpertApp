package com.neotelemetrixgdscunand.kamekapp.presentation.ui.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neotelemetrixgdscunand.kamekapp.domain.common.Result
import com.neotelemetrixgdscunand.kamekapp.domain.data.NewsRepository
import com.neotelemetrixgdscunand.kamekapp.domain.model.NewsType
import com.neotelemetrixgdscunand.kamekapp.presentation.dui.NewsItemDui
import com.neotelemetrixgdscunand.kamekapp.presentation.mapper.DuiMapper
import com.neotelemetrixgdscunand.kamekapp.presentation.utils.UIText
import com.neotelemetrixgdscunand.kamekapp.presentation.utils.toErrorUIText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
    private val duiMapper: DuiMapper
) : ViewModel() {

    private val _newsItems = MutableStateFlow<ImmutableList<NewsItemDui>>(persistentListOf())
    val newsItems = _newsItems.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    private val _onMessageEvent = Channel<UIText>()
    val onMessageEvent = _onMessageEvent.receiveAsFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _newsType = MutableStateFlow(NewsType.COCOA)
    val newsType = _newsType.asStateFlow()
    private var previousNewsTypeValue = _newsType.value

    private val searchQueryDebounceDuration = 1000L

    init {
        viewModelScope.launch {
            combine(_searchQuery, _newsType) { query, newsType ->
                Pair(query, newsType)
            }
                .collectLatest { (query, newsType) ->
                    val shouldDebounce = previousNewsTypeValue == newsType
                    previousNewsTypeValue = newsType
                    if (shouldDebounce) {
                        delay(searchQueryDebounceDuration)
                    }

                    try {
                        _isLoading.update { true }
                        getNewsItems(query, newsType)
                    } finally {
                        _isLoading.update { false }
                    }
                }
        }
    }

    fun onQueryChange(newQuery: String) {
        _searchQuery.update { newQuery }
    }

    fun onNewsTypeChange(newsType: NewsType) {
        _searchQuery.update { "" }
        _newsType.update { newsType }
    }

    private suspend fun getNewsItems(query: String = "", newsType: NewsType) {
        when (val result = newsRepository.getNewsItems(
            query = query,
            newsType = newsType
        )
        ) {
            is Result.Error -> {
                coroutineContext.ensureActive()
                val errorUIText = result.toErrorUIText()
                _onMessageEvent.send(errorUIText)
            }

            is Result.Success -> {
                coroutineContext.ensureActive()
                val newsItemsDui = result.data.map {
                    duiMapper.mapNewsItemToNewsItemDui(it)
                }.toImmutableList()

                _newsItems.update { newsItemsDui }
            }
        }
    }

}