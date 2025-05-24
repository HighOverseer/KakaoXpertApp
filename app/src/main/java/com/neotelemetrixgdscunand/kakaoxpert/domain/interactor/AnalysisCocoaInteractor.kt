package com.neotelemetrixgdscunand.kakaoxpert.domain.interactor

import com.neotelemetrixgdscunand.kakaoxpert.domain.common.CocoaAnalysisError
import com.neotelemetrixgdscunand.kakaoxpert.domain.common.DataError
import com.neotelemetrixgdscunand.kakaoxpert.domain.common.Result
import com.neotelemetrixgdscunand.kakaoxpert.domain.data.CocoaAnalysisRepository
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.AnalysisSession
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.BoundingBox
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.CocoaDisease
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.DetectedCocoa
import com.neotelemetrixgdscunand.kakaoxpert.domain.presentation.CocoaDamageLevelPredictionHelper
import com.neotelemetrixgdscunand.kakaoxpert.domain.presentation.CocoaDamageLevelPredictionResult
import com.neotelemetrixgdscunand.kakaoxpert.domain.presentation.CocoaDiseaseDetectorHelper
import com.neotelemetrixgdscunand.kakaoxpert.domain.presentation.CocoaPriceCalculationHelper
import com.neotelemetrixgdscunand.kakaoxpert.domain.presentation.ImageDetectorResult
import com.neotelemetrixgdscunand.kakaoxpert.domain.usecase.AnalysisCocoaUseCase
import kotlinx.coroutines.ensureActive
import kotlin.coroutines.coroutineContext

class AnalysisCocoaInteractor(
    private val cacaoImageDetectorHelper: CocoaDiseaseDetectorHelper,
    private val cocoaDamageLevelPredictionHelper: CocoaDamageLevelPredictionHelper,
    private val cocoaPriceCalculationHelper: CocoaPriceCalculationHelper,
    private val cocoaAnalysisRepository: CocoaAnalysisRepository,
) : AnalysisCocoaUseCase {
    override suspend fun invoke(
        sessionName: String,
        imagePath: String
    ): Result<AnalysisSession, DataError> {
        val result = try {
            cacaoImageDetectorHelper.detect(imagePath)
        } finally {
            cacaoImageDetectorHelper.clearResource()
        }

        val boundingBoxes = mutableListOf<BoundingBox>()
        when (result) {
            ImageDetectorResult.NoObjectDetected -> {
                return Result.Error(CocoaAnalysisError.NO_COCOA_DETECTED)
            }

            is ImageDetectorResult.Error -> {
                return Result.Error(CocoaAnalysisError.FAILED_TO_DETECT_COCOA)
            }

            is ImageDetectorResult.Success -> {
                boundingBoxes.addAll(result.boundingBoxes)
            }
        }

        coroutineContext.ensureActive()

        val predictedDamageLevels = mutableListOf<Float>()
        val predictedDamageLevelResult = try {
            cocoaDamageLevelPredictionHelper.predict(imagePath, boundingBoxes)
        } finally {
            cocoaDamageLevelPredictionHelper.cleanResource()
        }

        when (predictedDamageLevelResult) {
            is CocoaDamageLevelPredictionResult.Error -> {
                return Result.Error(CocoaAnalysisError.FAILED_TO_DETECT_COCOA)
            }

            is CocoaDamageLevelPredictionResult.Success -> {
                if (boundingBoxes.size != predictedDamageLevelResult.damageLevels.size) {
                    return Result.Error(CocoaAnalysisError.FAILED_TO_DETECT_COCOA)
                }

                predictedDamageLevels.addAll(predictedDamageLevelResult.damageLevels)
            }
        }

        coroutineContext.ensureActive()

        val boundingBoxDamageLevels = boundingBoxes.zip(predictedDamageLevels)
        val calculatedCocoaSellPrices = cocoaPriceCalculationHelper.calculate(
            boundingBoxDamageLevels
        )

        if (boundingBoxes.size != calculatedCocoaSellPrices.size) {
            return Result.Error(CocoaAnalysisError.FAILED_TO_DETECT_COCOA)
        }

        val detectedCocoas = boundingBoxDamageLevels.zip(
            calculatedCocoaSellPrices
        ).mapIndexed { index, (boundingBoxAndDamageLevels, sellPrice) ->
            val (boundingBox, damageLevel) = boundingBoxAndDamageLevels

            DetectedCocoa(
                cacaoNumber = index.plus(1).toShort(),
                boundingBox = boundingBox,
                disease = CocoaDisease.getDiseaseFromName(
                    name = boundingBox.label
                ),
                damageLevel = damageLevel,
                predictedPriceInIdr = sellPrice,
            )
        }

        val totalPredictedPrice = detectedCocoas.sumOf { it.predictedPriceInIdr.toDouble() }

        val newSessionId = cocoaAnalysisRepository.saveDiagnosis(
            sessionName = sessionName,
            imageOrUrlPath = imagePath,
            predictedPrice = totalPredictedPrice.toFloat(),
            detectedCocoas = detectedCocoas
        )

        return cocoaAnalysisRepository.getDiagnosisSession(newSessionId)
    }
}