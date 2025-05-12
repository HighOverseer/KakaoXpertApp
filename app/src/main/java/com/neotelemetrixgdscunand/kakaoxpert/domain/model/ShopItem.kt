package com.neotelemetrixgdscunand.kakaoxpert.domain.model

import kotlin.random.Random

data class ShopItem(
    val id: Int = Random.nextInt(0, 1_000_000),
    val imageUrl: String,
    val title: String,
    val price: Long,
    val targetUrl: String
)