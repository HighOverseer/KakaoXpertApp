package com.neotelemetrixgdscunand.kakaoxpert.data

import com.neotelemetrixgdscunand.kakaoxpert.data.remote.ApiService
import com.neotelemetrixgdscunand.kakaoxpert.data.utils.fetchFromNetwork
import com.neotelemetrixgdscunand.kakaoxpert.domain.common.DataError
import com.neotelemetrixgdscunand.kakaoxpert.domain.common.Result
import com.neotelemetrixgdscunand.kakaoxpert.domain.common.RootNetworkError
import com.neotelemetrixgdscunand.kakaoxpert.domain.data.NewsRepository
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.NewsDetails
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.NewsItem
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.NewsType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.coroutineContext

@Singleton
class NewsRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val mapper: DataMapper
) : NewsRepository {

    override suspend fun getNewsItems(
        query: String,
        newsType: NewsType
    ): Result<List<NewsItem>, DataError.NetworkError> {
        return fetchFromNetwork {
            val response = apiService.getNewsItems(query, newsType.id)
            coroutineContext.ensureActive()
            val newsItemsDto = response.data ?: emptyList()

            val newsItem = newsItemsDto.mapNotNull {
                mapper.mapNewsItemToDomain(it)
            }

            coroutineContext.ensureActive()

            Result.Success(newsItem)
        }
    }

    override suspend fun getNewsItemsPreviews(newsType: NewsType): Result<List<NewsItem>, DataError.NetworkError> {
        return when (val result = getNewsItems(newsType = newsType)) {
            is Result.Success -> Result.Success(result.data.take(5))
            is Result.Error -> result
        }
    }

    override suspend fun getNewsById(
        newsId: Int,
        newsType: NewsType
    ): Result<NewsDetails, DataError.NetworkError> {
        return fetchFromNetwork {
            val response = apiService.getNewsById(newsId, newsType.id)
            coroutineContext.ensureActive()
            val newsDetailsDto = response.data ?: return@fetchFromNetwork Result.Error(
                RootNetworkError.UNEXPECTED_ERROR
            )
            val newsDetails = withContext(Dispatchers.Default) {
                mapper.mapNewsDetailsToDomain(newsDetailsDto)
            } ?: return@fetchFromNetwork Result.Error(RootNetworkError.UNEXPECTED_ERROR)

            coroutineContext.ensureActive()

            return@fetchFromNetwork Result.Success(newsDetails)
        }
    }
}