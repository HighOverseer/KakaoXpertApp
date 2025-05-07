package com.neotelemetrixgdscunand.kamekapp.domain.model

data class NewsItem(
    val id: Int,
    val time: Long,
    val imageUrl: String,
    val headline: String
)