package com.neotelemetrixgdscunand.kakaoxpert.data.local.database

import com.neotelemetrixgdscunand.kakaoxpert.BuildConfig
import com.neotelemetrixgdscunand.kakaoxpert.data.local.database.entity.CocoaAnalysisPreviewEntity
import com.neotelemetrixgdscunand.kakaoxpert.data.local.database.entity.SavedCocoaAnalysisEntity
import com.neotelemetrixgdscunand.kakaoxpert.data.local.database.entity.SavedDetectedCocoaEntity
import com.neotelemetrixgdscunand.kakaoxpert.data.local.database.entity.UnsavedDetectedCocoaEntity
import com.neotelemetrixgdscunand.kakaoxpert.data.remote.dto.AnalysisSessionDto
import com.neotelemetrixgdscunand.kakaoxpert.data.remote.dto.AnalysisSessionPreviewDto
import com.neotelemetrixgdscunand.kakaoxpert.data.remote.dto.DetectedCocoaDto
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.BoundingBox
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.CocoaDisease
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.DetectedCocoa
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

object EntityMapper {
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
            predictedPrice = 2100f
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
            userId = analysisSessionDto.userId ?: return null,
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
            damagePercentage = 0.5f,
            damageLevel = 50
        )
    }

}