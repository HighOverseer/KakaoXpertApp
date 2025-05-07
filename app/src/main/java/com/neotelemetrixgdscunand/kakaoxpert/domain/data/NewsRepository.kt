package com.neotelemetrixgdscunand.kamekapp.domain.data

import com.neotelemetrixgdscunand.kamekapp.domain.common.DataError
import com.neotelemetrixgdscunand.kamekapp.domain.common.Result
import com.neotelemetrixgdscunand.kamekapp.domain.model.NewsDetails
import com.neotelemetrixgdscunand.kamekapp.domain.model.NewsItem
import com.neotelemetrixgdscunand.kamekapp.domain.model.NewsType

interface NewsRepository {

    suspend fun getNewsItems(
        query: String = "",
        newsType: NewsType
    ): Result<List<NewsItem>, DataError.NetworkError>

    suspend fun getNewsItemsPreviews(newsType: NewsType): Result<List<NewsItem>, DataError.NetworkError>

    suspend fun getNewsById(
        newsId: Int,
        newsType: NewsType
    ): Result<NewsDetails, DataError.NetworkError>

}