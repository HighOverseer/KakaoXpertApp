package com.neotelemetrixgdscunand.kakaoxpert.presentation.mapper

import com.neotelemetrixgdscunand.kakaoxpert.R
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.AnalysisSession
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.AnalysisSessionPreview
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.CocoaAverageSellPriceInfo
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.IoTDataOverview
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.NewsDetails
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.NewsItem
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.ShopItem
import com.neotelemetrixgdscunand.kakaoxpert.presentation.dui.AnalysisSessionDui
import com.neotelemetrixgdscunand.kakaoxpert.presentation.dui.AnalysisSessionPreviewDui
import com.neotelemetrixgdscunand.kakaoxpert.presentation.dui.CocoaAverageSellPriceInfoDui
import com.neotelemetrixgdscunand.kakaoxpert.presentation.dui.IoTDataOverviewDui
import com.neotelemetrixgdscunand.kakaoxpert.presentation.dui.NewsDetailsDui
import com.neotelemetrixgdscunand.kakaoxpert.presentation.dui.NewsItemDui
import com.neotelemetrixgdscunand.kakaoxpert.presentation.dui.ShopItemDui
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.util.formatFloat
import com.neotelemetrixgdscunand.kakaoxpert.presentation.utils.UIText
import kotlinx.collections.immutable.toImmutableList
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object DuiMapper {
    private const val NEWS_ITEM_DATE_FORMAT = "d MMM yyyy"

    fun mapNewsItemToDui(
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

    fun mapIoTDataOverviewToDui(
        ioTDataOverview: IoTDataOverview
    ): IoTDataOverviewDui {
        return IoTDataOverviewDui(
            averageTemperatureValue = UIText.StringResource(
                R.string.suhu_format,
                arrayOf(ioTDataOverview.averageTemperatureValue?.formatFloat()?.toString() ?: "-")
            ),
            averageHumidityValue = UIText.StringResource(
                R.string.humidity_format,
                arrayOf(ioTDataOverview.averageHumidityValue?.formatFloat()?.toString() ?: "-")
            ),
            averageLightIntensityValue = UIText.StringResource(
                R.string.light_intensity_format,
                arrayOf(
                    ioTDataOverview.averageLightIntensityValue?.formatFloat()?.toString() ?: "-"
                )
            ),
        )
    }

    fun mapNewsDetailsToDui(
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

    fun mapDiagnosisSessionToDui(analysisSession: AnalysisSession): AnalysisSessionDui {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = analysisSession.createdAt
        val sdf = SimpleDateFormat("d-MM-yyyy", Locale.getDefault())
        val dateString = sdf.format(calendar.time)

        return AnalysisSessionDui(
            id = analysisSession.id,
            title = analysisSession.title,
            imageUrlOrPath = analysisSession.imageUrlOrPath,
            date = dateString,
            predictedPrice = analysisSession.predictedPrice,
            detectedCocoas = analysisSession.detectedCocoas.toImmutableList(),
            solutionEn = analysisSession.solutionEn,
            preventionsEn = analysisSession.preventionsEn,
            solutionId = analysisSession.solutionId,
            preventionsId = analysisSession.preventionsId
        )
    }

    fun mapDiagnosisSessionPreviewToDui(analysisSessionPreview: AnalysisSessionPreview): AnalysisSessionPreviewDui {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = analysisSessionPreview.createdAt
        val sdf = SimpleDateFormat("d-MM-yyyy", Locale.getDefault())
        val dateString = sdf.format(calendar.time)

        return AnalysisSessionPreviewDui(
            id = analysisSessionPreview.id,
            title = analysisSessionPreview.title,
            imageUrlOrPath = analysisSessionPreview.imageUrlOrPath,
            date = dateString,
            predictedPrice = analysisSessionPreview.predictedPrice,
            hasSynced = analysisSessionPreview.hasSynced,
            availableOffline = analysisSessionPreview.isDetailAvailableInLocal
        )
    }

    fun mapCocoaAverageSellPriceInfoToDui(
        cocoaAverageSellPriceInfo: CocoaAverageSellPriceInfo
    ):CocoaAverageSellPriceInfoDui{
        val formatter = NumberFormat.getCurrencyInstance(Locale("in", "ID"))

        val currentAveragePrice = cocoaAverageSellPriceInfo.currentAveragePrice.formatFloat().let {
            formatter.format(it)
                .replace("Rp", "Rp ")
        }
        val previousAveragePriceFormatted = cocoaAverageSellPriceInfo.previousAveragePrice?.formatFloat()?.let {
            formatter.format(it)
                .replace("Rp", "Rp ")
        }

        return CocoaAverageSellPriceInfoDui(
            currentAveragePrice = currentAveragePrice,
            previousAveragePrice = previousAveragePriceFormatted,
            rateFromPrevious = cocoaAverageSellPriceInfo.rateFromPrevious?.times(100f)?.formatFloat()?.toString()
        )
    }

}