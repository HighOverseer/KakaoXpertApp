package com.neotelemetrixgdscunand.kakaoxpert.domain.data

import com.neotelemetrixgdscunand.kakaoxpert.domain.common.DataError
import com.neotelemetrixgdscunand.kakaoxpert.domain.common.Result
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.NewsDetails
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.NewsItem
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.NewsType

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