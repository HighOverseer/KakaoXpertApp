package com.neotelemetrixgdscunand.kamekapp.presentation.mapper

import com.neotelemetrixgdscunand.kamekapp.domain.model.NewsDetails
import com.neotelemetrixgdscunand.kamekapp.domain.model.NewsItem
import com.neotelemetrixgdscunand.kamekapp.domain.model.ShopItem
import com.neotelemetrixgdscunand.kamekapp.presentation.dui.NewsDetailsDui
import com.neotelemetrixgdscunand.kamekapp.presentation.dui.NewsItemDui
import com.neotelemetrixgdscunand.kamekapp.presentation.dui.ShopItemDui
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object DuiMapper {
    private const val NEWS_ITEM_DATE_FORMAT = "d MMM yyyy"

    fun mapNewsItemToNewsItemDui(
        newsItem: NewsItem
    ): NewsItemDui {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = newsItem.time

        val sdf = SimpleDateFormat(NEWS_ITEM_DATE_FORMAT, Locale.getDefault())
        val dateString = sdf.format(calendar.time)


        return NewsItemDui(
            id = newsItem.id,
            date = dateString,
            imageUrl = newsItem.imageUrl,
            headline = newsItem.headline
        )
    }

    fun mapNewsDetailsToNewsDetailsDui(
        newsDetails: NewsDetails
    ): NewsDetailsDui {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = newsDetails.time

        val sdf = SimpleDateFormat(NEWS_ITEM_DATE_FORMAT, Locale.getDefault())
        val dateString = sdf.format(calendar.time)

        return NewsDetailsDui(
            date = dateString,
            imageUrl = newsDetails.imageUrl,
            headline = newsDetails.headline,
            description = newsDetails.description
        )
    }

    fun mapShopItemToShopItemDui(
        shopItem: ShopItem
    ): ShopItemDui {
        return ShopItemDui(
            id = shopItem.id,
            imageUrl = shopItem.imageUrl,
            title = shopItem.title,
            price = "Rp${shopItem.price}",
            targetUrl = shopItem.targetUrl
        )
    }
}