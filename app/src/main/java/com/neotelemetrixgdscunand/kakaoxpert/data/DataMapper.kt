package com.neotelemetrixgdscunand.kakaoxpert.data

import com.neotelemetrixgdscunand.kakaoxpert.BuildConfig
import com.neotelemetrixgdscunand.kakaoxpert.data.remote.dto.AnalysisSessionDto
import com.neotelemetrixgdscunand.kakaoxpert.data.remote.dto.DetectedCocoaDto
import com.neotelemetrixgdscunand.kakaoxpert.data.remote.dto.IoTDataDto
import com.neotelemetrixgdscunand.kakaoxpert.data.remote.dto.IoTDataOverviewDto
import com.neotelemetrixgdscunand.kakaoxpert.data.remote.dto.IoTDeviceDto
import com.neotelemetrixgdscunand.kakaoxpert.data.remote.dto.NewsDetailsDto
import com.neotelemetrixgdscunand.kakaoxpert.data.remote.dto.NewsItemDto
import com.neotelemetrixgdscunand.kakaoxpert.data.remote.dto.ShopItemDto
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.AnalysisSession
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.BoundingBox
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.CocoaDisease
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.DetectedCocoa
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.IoTDataOverview
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.IoTDevice
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.NewsDetails
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.NewsItem
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.SensorItemData
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.ShopItem
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

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

    fun mapIoTDeviceDtoToDomain(ioTDeviceDto: IoTDeviceDto): IoTDevice? {
        return IoTDevice(
            id = ioTDeviceDto.iotDeviceId ?: return null,
            name = "IoT Device ${ioTDeviceDto.iotDeviceId}"
        )
    }

    fun mapIoTDataOverviewDtoToDomain(
        ioTDataOverviewDto: IoTDataOverviewDto
    ): IoTDataOverview {
        return IoTDataOverview(
            averageTemperatureValue = ioTDataOverviewDto.temperatureValue,
            averageHumidityValue = ioTDataOverviewDto.humidityValue,
            averageLightIntensityValue = ioTDataOverviewDto.lightIntensityValue
        )
    }

    fun mapIoTDataDtoToDomain(
        ioTDataDto: IoTDataDto
    ): Triple<SensorItemData.Temperature, SensorItemData.Humidity, SensorItemData.LightIntensity>? {

        val dateDateString = ioTDataDto.date ?: return null
        val datePattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
        val sdf = SimpleDateFormat(datePattern, Locale.getDefault())
        sdf.timeZone = TimeZone.getTimeZone("UTC")

        val date = try {
            sdf.parse(dateDateString)
        } catch (e: ParseException) {
            return null
        }

        val calendar = Calendar.getInstance()
        calendar.time = date
        val timeInMillis = calendar.timeInMillis

        val temperatureSensorData = SensorItemData.Temperature(
            value = ioTDataDto.temperatureValue ?: return null,
            timeInMillis = timeInMillis
        )
        val humiditySensorData =
            SensorItemData.Humidity(value = ioTDataDto.humidityValue ?: return null, timeInMillis)
        val lightIntensitySensorData = SensorItemData.LightIntensity(
            value = ioTDataDto.lightIntensityValue ?: return null,
            timeInMillis
        )

        return Triple(
            temperatureSensorData,
            humiditySensorData,
            lightIntensitySensorData
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

    fun mapAnalysisSessionDtoToDomain(
        analysisSessionDto: AnalysisSessionDto
    ): AnalysisSession? {

        val dateDateString = analysisSessionDto.date ?: return null
        val datePattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
        val sdf = SimpleDateFormat(datePattern, Locale.getDefault())
        sdf.timeZone = TimeZone.getTimeZone("UTC")

        val date = try {
            sdf.parse(dateDateString)
        } catch (e: ParseException) {
            return null
        }

        val calendar = Calendar.getInstance()
        calendar.time = date
        val createdAt = calendar.timeInMillis

        val imageUrl = "${BuildConfig.IMAGE_BASE_URL}${analysisSessionDto.sessionImage}"

        return AnalysisSession(
            id = analysisSessionDto.sessionId ?: return null,
            title = analysisSessionDto.sessionName ?: return null,
            createdAt = createdAt,
            imageUrlOrPath = imageUrl,
            predictedPrice = 2100f,
            solutionEn = analysisSessionDto.solutionEn,
            preventionsEn = analysisSessionDto.preventionEn,
            solutionId = analysisSessionDto.solutionId,
            preventionsId = analysisSessionDto.preventionId,
            detectedCocoas = analysisSessionDto.detectedCocoas?.mapNotNull {
                mapDetectedCocoaDtoToDomain(it)
            } ?: return null
        )
    }

    fun mapDetectedCocoaDtoToDomain(
        detectedCocoaDto: DetectedCocoaDto?
    ): DetectedCocoa? {
        return DetectedCocoa(
            id = detectedCocoaDto?.id ?: return null,
            cacaoNumber = detectedCocoaDto.cocoaNumber?.toShort() ?: return null,
            boundingBox = BoundingBox(
                x1 = detectedCocoaDto.bbCoordinateLeft ?: return null,
                y1 = detectedCocoaDto.bbCoordinateTop ?: return null,
                x2 = detectedCocoaDto.bbCoordinateRight ?: return null,
                y2 = detectedCocoaDto.bbCoordinateBottom ?: return null,
                cx = detectedCocoaDto.bbCenterX ?: return null,
                cy = detectedCocoaDto.bbCenterY ?: return null,
                w = detectedCocoaDto.bbWidth ?: return null,
                h = detectedCocoaDto.bbHeight ?: return null,
                label = detectedCocoaDto.bbLabel ?: return null,
                cls = detectedCocoaDto.bbCls ?: return null,
                cnf = detectedCocoaDto.bbConfidence ?: return null
            ),
            disease = CocoaDisease.getDiseaseFromId(detectedCocoaDto.diseaseId ?: return null)
                ?: return null
        )
    }


//    fun mapCocoaAnalysisSessionPreviewDtoToDomain(
//        analysisSessionPreviewDto: AnalysisSessionPreviewDto
//    ): AnalysisSessionPreview? {
//        val dateDateString = analysisSessionPreviewDto.date ?: return null
//        val datePattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
//        val sdf = SimpleDateFormat(datePattern, Locale.getDefault())
//        sdf.timeZone = TimeZone.getTimeZone("UTC")
//
//        val date = try {
//            sdf.parse(dateDateString)
//        } catch (e: ParseException) {
//            return null
//        }
//
//        val calendar = Calendar.getInstance()
//        calendar.time = date
//        val createdAt = calendar.timeInMillis
//
//        val imageUrl = "${BuildConfig.IMAGE_BASE_URL}${analysisSessionPreviewDto.sessionImage}"
//
//        return AnalysisSessionPreview(
//            id = analysisSessionPreviewDto.sessionId ?: return null,
//            title = analysisSessionPreviewDto.sessionName ?: return null,
//            imageUrlOrPath = imageUrl,
//            createdAt = createdAt,
//            predictedPrice = 2100f
//        )
//    }

//    fun mapAnalysisSessionPreviewToEntity(
//        cocoaAnalysisSessionPreview: AnalysisSessionPreview
//    ): CocoaAnalysisPreviewEntity {
//        return CocoaAnalysisPreviewEntity(
//            sessionId = cocoaAnalysisSessionPreview.id,
//            createdAt = cocoaAnalysisSessionPreview.createdAt,
//            sessionName = cocoaAnalysisSessionPreview.title,
//            sessionImageUrl = cocoaAnalysisSessionPreview.imageUrlOrPath,
//        )
//    }

}