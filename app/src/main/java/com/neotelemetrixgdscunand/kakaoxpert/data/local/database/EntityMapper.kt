package com.neotelemetrixgdscunand.kakaoxpert.data.local.database

import com.neotelemetrixgdscunand.kakaoxpert.BuildConfig
import com.neotelemetrixgdscunand.kakaoxpert.data.local.database.entity.CocoaAnalysisPreviewEntity
import com.neotelemetrixgdscunand.kakaoxpert.data.local.database.entity.CocoaAverageSellPriceHistoryEntity
import com.neotelemetrixgdscunand.kakaoxpert.data.local.database.entity.CocoaDiseaseSellPriceInfoEntity
import com.neotelemetrixgdscunand.kakaoxpert.data.local.database.entity.SavedCocoaAnalysisEntity
import com.neotelemetrixgdscunand.kakaoxpert.data.local.database.entity.SavedDetectedCocoaEntity
import com.neotelemetrixgdscunand.kakaoxpert.data.local.database.entity.UnsavedCocoaAnalysisEntity
import com.neotelemetrixgdscunand.kakaoxpert.data.local.database.entity.UnsavedDetectedCocoaEntity
import com.neotelemetrixgdscunand.kakaoxpert.data.local.database.entity.relations.CocoaAnalysisPreviewRelation
import com.neotelemetrixgdscunand.kakaoxpert.data.remote.dto.AnalysisSessionDto
import com.neotelemetrixgdscunand.kakaoxpert.data.remote.dto.AnalysisSessionPreviewDto
import com.neotelemetrixgdscunand.kakaoxpert.data.remote.dto.CocoaDiseaseSellPriceInfoDto
import com.neotelemetrixgdscunand.kakaoxpert.data.remote.dto.CocoaSellPriceInfoDto
import com.neotelemetrixgdscunand.kakaoxpert.data.remote.dto.DetectedCocoaDto
import com.neotelemetrixgdscunand.kakaoxpert.domain.data.DataPreference
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.AnalysisSession
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.AnalysisSessionPreview
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.BoundingBox
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.CocoaDisease
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.DetectedCocoa
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

object EntityMapper {

    fun mapCocoaSellPriceInfoDtoToEntity(
        cocoaSellPriceInfoDto: CocoaSellPriceInfoDto
    ): CocoaAverageSellPriceHistoryEntity?{
        return CocoaAverageSellPriceHistoryEntity(
            currentAveragePrice = cocoaSellPriceInfoDto.averageSellPrice ?: return null,
            previousAveragePrice = cocoaSellPriceInfoDto.averageSellPricePrevious,
            rateFromPrevious = cocoaSellPriceInfoDto.rateFromPrevious,
            time = System.currentTimeMillis()
        )
    }

    fun mapCocoaDiseasePriceInfoDtoToEntity(
        cocoaDiseaseSellPriceInfoDto: CocoaDiseaseSellPriceInfoDto
    ):CocoaDiseaseSellPriceInfoEntity?{
        return CocoaDiseaseSellPriceInfoEntity(
            diseaseId = cocoaDiseaseSellPriceInfoDto.diseaseId ?: return null,
            highestPrice = cocoaDiseaseSellPriceInfoDto.highestPrice ?: return null,
            lowestPrice = cocoaDiseaseSellPriceInfoDto.lowestPrice ?: return null,
            decreasingRatePerDamageLevel = cocoaDiseaseSellPriceInfoDto.sellPriceDecreasingRatePerOnePercentDamageLevel ?: return null
        )
    }


    fun mapCocoaAnalysisSessionPreviewDtoToEntity(
        analysisSessionPreviewDto: AnalysisSessionPreviewDto
    ): CocoaAnalysisPreviewEntity? {
        val dateDateString = analysisSessionPreviewDto.date ?: return null
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

        val imageUrl = "${BuildConfig.IMAGE_BASE_URL}${analysisSessionPreviewDto.sessionImage}"

        return CocoaAnalysisPreviewEntity(
            sessionId = analysisSessionPreviewDto.sessionId ?: return null,
            sessionName = analysisSessionPreviewDto.sessionName ?: return null,
            sessionImageUrl = imageUrl,
            createdAt = createdAt,
            predictedPrice = 2100f,
            isDeleted = false
        )
    }

    fun mapUnsavedDetectedCocoaEntityToDomain(
        unsavedDetectedCocoaEntity: UnsavedDetectedCocoaEntity
    ): DetectedCocoa? {
        return DetectedCocoa(
            id = unsavedDetectedCocoaEntity.unsavedId,
            cacaoNumber = unsavedDetectedCocoaEntity.cocoaNumber.toShort(),
            boundingBox = BoundingBox(
                x1 = unsavedDetectedCocoaEntity.bbCoordinateLeft,
                y1 = unsavedDetectedCocoaEntity.bbCoordinateTop,
                x2 = unsavedDetectedCocoaEntity.bbCoordinateRight,
                y2 = unsavedDetectedCocoaEntity.bbCoordinateBottom,
                w = unsavedDetectedCocoaEntity.bbWidth,
                h = unsavedDetectedCocoaEntity.bbHeight,
                cx = unsavedDetectedCocoaEntity.bbCenterX,
                cy = unsavedDetectedCocoaEntity.bbCenterY,
                cls = unsavedDetectedCocoaEntity.bbCls,
                cnf = unsavedDetectedCocoaEntity.bbConfidence,
                label = unsavedDetectedCocoaEntity.bbLabel
            ),
            disease = CocoaDisease.getDiseaseFromId(unsavedDetectedCocoaEntity.diseaseId)
                ?: return null
        )
    }

    fun mapDetectedCocoaToUnsavedEntity(
        detectedCocoa: DetectedCocoa,
        unsavedSessionId: Int
    ): UnsavedDetectedCocoaEntity {
        return UnsavedDetectedCocoaEntity(
            unsavedSessionId = unsavedSessionId,
            cocoaNumber = detectedCocoa.cacaoNumber.toInt(),
            bbCoordinateLeft = detectedCocoa.boundingBox.x1,
            bbCoordinateTop = detectedCocoa.boundingBox.y1,
            bbCoordinateRight = detectedCocoa.boundingBox.x2,
            bbCoordinateBottom = detectedCocoa.boundingBox.y2,
            bbCenterX = detectedCocoa.boundingBox.cx,
            bbCenterY = detectedCocoa.boundingBox.cy,
            bbWidth = detectedCocoa.boundingBox.w,
            bbHeight = detectedCocoa.boundingBox.h,
            bbLabel = detectedCocoa.boundingBox.label,
            bbCls = detectedCocoa.boundingBox.cls,
            bbConfidence = detectedCocoa.boundingBox.cnf,
            diseaseId = detectedCocoa.disease.id,
            varietyInfoId = 1,
            damageLevel = 0.5f
        )
    }


    fun mapCocoaAnalysisDtoToSavedEntity(
        analysisSessionDto: AnalysisSessionDto,
    ): SavedCocoaAnalysisEntity? {

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

        val sessionId = analysisSessionDto.sessionId ?: return null
        return SavedCocoaAnalysisEntity(
            sessionId = sessionId,
            //userId = analysisSessionDto.userId ?: return null,
            sessionName = analysisSessionDto.sessionName ?: return null,
            createdAt = createdAt,
            sessionImageUrl = imageUrl,
            predictedPrice = 2100f,
            solutionEn = analysisSessionDto.solutionEn ?: return null,
            preventionsEn = analysisSessionDto.preventionEn ?: return null,
            solutionId = analysisSessionDto.solutionId ?: return null,
            preventionsId = analysisSessionDto.preventionId ?: return null,

            )
    }

    fun mapDetectedCocoaDtoToSavedEntity(
        detectedCocoaDto: DetectedCocoaDto?,
        sessionId: Int
    ): SavedDetectedCocoaEntity? {
        return SavedDetectedCocoaEntity(
            id = detectedCocoaDto?.id ?: return null,
            cocoaNumber = detectedCocoaDto.cocoaNumber ?: return null,
            bbCoordinateLeft = detectedCocoaDto.bbCoordinateLeft ?: return null,
            bbCoordinateTop = detectedCocoaDto.bbCoordinateTop ?: return null,
            bbCoordinateRight = detectedCocoaDto.bbCoordinateRight ?: return null,
            bbCoordinateBottom = detectedCocoaDto.bbCoordinateBottom ?: return null,
            bbCenterX = detectedCocoaDto.bbCenterX ?: return null,
            bbCenterY = detectedCocoaDto.bbCenterY ?: return null,
            bbWidth = detectedCocoaDto.bbWidth ?: return null,
            bbHeight = detectedCocoaDto.bbHeight ?: return null,
            bbLabel = detectedCocoaDto.bbLabel ?: return null,
            bbCls = detectedCocoaDto.bbCls ?: return null,
            bbConfidence = detectedCocoaDto.bbConfidence ?: return null,
            diseaseId = detectedCocoaDto.diseaseId ?: return null,
            varietyInfoId = 1,
            sessionId = sessionId,
            damageLevel = detectedCocoaDto.damageLevel ?: return null
        )
    }

    fun mapUnsavedCocoaAnalysisEntityToDomain(
        unsavedCocoaAnalysisEntity: UnsavedCocoaAnalysisEntity,
        unsavedDetectedCocoas: List<UnsavedDetectedCocoaEntity>
    ): AnalysisSession {
        val detectedCocoas = unsavedDetectedCocoas.mapNotNull {
            mapUnsavedDetectedCocoaEntityToDomain(it)
        }
        return AnalysisSession(
            id = unsavedCocoaAnalysisEntity.unsavedSessionId,
            title = unsavedCocoaAnalysisEntity.sessionName,
            imageUrlOrPath = unsavedCocoaAnalysisEntity.sessionImagePath,
            createdAt = unsavedCocoaAnalysisEntity.createdAt,
            predictedPrice = 2100f,
            detectedCocoas = detectedCocoas,
//            solutionId = unsavedCocoaAnalysisEntity.solutionId,
//            preventionsId = unsavedCocoaAnalysisEntity.preventionId,
//            solutionEn = unsavedCocoaAnalysisEntity.solutionEn,
//            preventionsEn = unsavedCocoaAnalysisEntity.preventionEn,
        )
    }

    fun mapSavedCocoaAnalysisEntityToDomain(
        savedCocoaAnalysisEntity: SavedCocoaAnalysisEntity,
        savedDetectedCocoas: List<SavedDetectedCocoaEntity>
    ): AnalysisSession {
        val detectedCocoas = savedDetectedCocoas.mapNotNull {
            mapSavedDetectedCocoaEntityToDomain(it)
        }

        return AnalysisSession(
            id = savedCocoaAnalysisEntity.sessionId,
            title = savedCocoaAnalysisEntity.sessionName,
            imageUrlOrPath = savedCocoaAnalysisEntity.sessionImageUrl,
            createdAt = savedCocoaAnalysisEntity.createdAt,
            predictedPrice = savedCocoaAnalysisEntity.predictedPrice,
            detectedCocoas = detectedCocoas,
            solutionId = savedCocoaAnalysisEntity.solutionId,
            preventionsId = savedCocoaAnalysisEntity.preventionsId,
            solutionEn = savedCocoaAnalysisEntity.solutionEn,
            preventionsEn = savedCocoaAnalysisEntity.preventionsEn,
        )
    }

    fun mapSavedDetectedCocoaEntityToDomain(
        savedDetectedCocoaEntity: SavedDetectedCocoaEntity
    ): DetectedCocoa? {
        return DetectedCocoa(
            id = savedDetectedCocoaEntity.id,
            cacaoNumber = savedDetectedCocoaEntity.cocoaNumber.toShort(),
            boundingBox = BoundingBox(
                x1 = savedDetectedCocoaEntity.bbCoordinateLeft,
                y1 = savedDetectedCocoaEntity.bbCoordinateTop,
                x2 = savedDetectedCocoaEntity.bbCoordinateRight,
                y2 = savedDetectedCocoaEntity.bbCoordinateBottom,
                w = savedDetectedCocoaEntity.bbWidth,
                h = savedDetectedCocoaEntity.bbHeight,
                cx = savedDetectedCocoaEntity.bbCenterX,
                cy = savedDetectedCocoaEntity.bbCenterY,
                cls = savedDetectedCocoaEntity.bbCls,
                cnf = savedDetectedCocoaEntity.bbConfidence,
                label = savedDetectedCocoaEntity.bbLabel
            ),
            disease = CocoaDisease.getDiseaseFromId(savedDetectedCocoaEntity.diseaseId)
                ?: return null
        )
    }

    fun mapCocoaAnalysisPreviewRelationToDomain(
        cocoaAnalysisPreviewRelation: CocoaAnalysisPreviewRelation
    ): AnalysisSessionPreview {
        val hasSynced = System.currentTimeMillis()
            .minus(cocoaAnalysisPreviewRelation.lastSyncedTime) < DataPreference.SYNC_TIME_PERIOD_IN_MILLIS

        return AnalysisSessionPreview(
            id = cocoaAnalysisPreviewRelation.sessionId,
            title = cocoaAnalysisPreviewRelation.sessionName,
            imageUrlOrPath = cocoaAnalysisPreviewRelation.sessionImageUrlOrPath,
            createdAt = cocoaAnalysisPreviewRelation.createdAt,
            predictedPrice = cocoaAnalysisPreviewRelation.predictedPrice,
            isDetailAvailableInLocal = cocoaAnalysisPreviewRelation.isDetailAvailableInLocalDB,
            hasSynced = hasSynced,
        )
    }

    fun mapSavedCocoaAnalysisEntityToCocoaAnalysisPreviewEntity(
        savedCocoaAnalysisEntity: SavedCocoaAnalysisEntity
    ): CocoaAnalysisPreviewEntity {
        return CocoaAnalysisPreviewEntity(
            sessionId = savedCocoaAnalysisEntity.sessionId,
            createdAt = savedCocoaAnalysisEntity.createdAt,
            sessionName = savedCocoaAnalysisEntity.sessionName,
            sessionImageUrl = savedCocoaAnalysisEntity.sessionImageUrl,
            predictedPrice = savedCocoaAnalysisEntity.predictedPrice,
            lastSyncedTime = System.currentTimeMillis(),
            isDeleted = false
        )
    }
}