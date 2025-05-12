package com.neotelemetrixgdscunand.kakaoxpert.data

import com.neotelemetrixgdscunand.kakaoxpert.data.remote.dto.NewsDetailsDto
import com.neotelemetrixgdscunand.kakaoxpert.data.remote.dto.NewsItemDto
import com.neotelemetrixgdscunand.kakaoxpert.data.remote.dto.ShopItemDto
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.NewsDetails
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.NewsItem
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.ShopItem
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

object DataMapper {
    private const val DATA_NEWS_DATE_PATTERN = "dd/MM/yyyy"
    private const val DATA_NEWS_LOCALE_LANGUAGE_TAG = "ID"
    fun mapNewsItemToDomain(newsItemDto: NewsItemDto): NewsItem? {
        val sdf = SimpleDateFormat(
            DATA_NEWS_DATE_PATTERN,
            Locale.forLanguageTag(
                DATA_NEWS_LOCALE_LANGUAGE_TAG
            )
        )
        val date = try {
            sdf.parse(newsItemDto.date ?: return null)
        } catch (e: ParseException) {
            return null
        }

        val time = date.time
        return NewsItem(
            id = newsItemDto.id ?: return null,
            time = time,
            imageUrl = newsItemDto.imageUrl ?: return null,
            headline = newsItemDto.headline ?: return null
        )
    }

    fun mapNewsDetailsToDomain(newsDetailsDto: NewsDetailsDto): NewsDetails? {
        val sdf = SimpleDateFormat(
            DATA_NEWS_DATE_PATTERN,
            Locale.forLanguageTag(
                DATA_NEWS_LOCALE_LANGUAGE_TAG
            )
        )
        val date = try {
            sdf.parse(newsDetailsDto.date ?: return null)
        } catch (e: ParseException) {
            return null
        }

        val time = date.time


        var cleanerDescription = extractCleanContent(newsDetailsDto.description ?: return null)
        cleanerDescription = cleanerDescription.replace(newsDetailsDto.headline ?: return null, "")

        return NewsDetails(
            time = time,
            imageUrl = newsDetailsDto.imageUrl ?: return null,
            headline = newsDetailsDto.headline,
            description = cleanerDescription
        )
    }

    private fun extractCleanContent(text: String): String {
        val cleanedStart = text.replaceFirst(Regex("^[\\n\\r\\t\\s]+"), "")
        return cleanedStart.replace(Regex("[\\n\\r\\t]+"), "")
    }

    fun mapShopItemDtoToDomain(
        shopItemDto: ShopItemDto
    ): ShopItem? {
        return ShopItem(
            id = shopItemDto.id ?: return null,
            imageUrl = shopItemDto.imageUrl ?: return null,
            title = shopItemDto.name ?: return null,
            price = shopItemDto.price ?: return null,
            targetUrl = shopItemDto.link ?: return null
        )
    }

}