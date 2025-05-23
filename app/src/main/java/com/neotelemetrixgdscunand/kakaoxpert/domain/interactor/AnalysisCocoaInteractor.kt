package com.neotelemetrixgdscunand.kakaoxpert.domain.interactor

import com.neotelemetrixgdscunand.kakaoxpert.domain.common.CocoaAnalysisError
import com.neotelemetrixgdscunand.kakaoxpert.domain.common.DataError
import com.neotelemetrixgdscunand.kakaoxpert.domain.common.Result
import com.neotelemetrixgdscunand.kakaoxpert.domain.data.CocoaAnalysisRepository
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.AnalysisSession
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.CocoaDisease
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.DetectedCocoa
import com.neotelemetrixgdscunand.kakaoxpert.domain.presentation.CocoaDamageLevelPredictionHelper
import com.neotelemetrixgdscunand.kakaoxpert.domain.presentation.CocoaDamageLevelPredictionResult
import com.neotelemetrixgdscunand.kakaoxpert.domain.presentation.CocoaDiseaseDetectorHelper
import com.neotelemetrixgdscunand.kakaoxpert.domain.presentation.ImageDetectorResult
import com.neotelemetrixgdscunand.kakaoxpert.domain.usecase.AnalysisCocoaUseCase
import kotlinx.coroutines.ensureActive
import kotlin.coroutines.coroutineContext

class AnalysisCocoaInteractor(
    private val cacaoImageDetectorHelper: CocoaDiseaseDetectorHelper,
    private val cocoaDamageLevelPredictionHelper: CocoaDamageLevelPredictionHelper,
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

        when (result) {
            ImageDetectorResult.NoObjectDetected -> {
                return Result.Error(CocoaAnalysisError.NO_COCOA_DETECTED)
            }

            is ImageDetectorResult.Error -> {
                return Result.Error(CocoaAnalysisError.FAILED_TO_DETECT_COCOA)
            }

            is ImageDetectorResult.Success -> {
                val detectedCocoas = result.boundingBoxes.mapIndexed { index, item ->
                    coroutineContext.ensureActive()

                    DetectedCocoa(
                        cacaoNumber = index.plus(1).toShort(),
                        boundingBox = item,
                        disease = CocoaDisease.getDiseaseFromName(
                            name = item.label
                        )
                    )
                }

                val predictedPricesResult =
                    cocoaDamageLevelPredictionHelper.predict(imagePath, result.boundingBoxes)
                when (predictedPricesResult) {
                    is CocoaDamageLevelPredictionResult.Error -> {
                        return Result.Error(CocoaAnalysisError.FAILED_TO_DETECT_COCOA)
                    }

                    is CocoaDamageLevelPredictionResult.Success -> {
                        val predictedPrices = predictedPricesResult.damageLevels
                        println(": ${result.boundingBoxes.zip(predictedPrices)}")
                    }
                }

                coroutineContext.ensureActive()

                val newSessionId = cocoaAnalysisRepository.saveDiagnosis(
                    sessionName = sessionName,
                    imageOrUrlPath = imagePath,
                    predictedPrice = 1680f,
                    detectedCocoas = detectedCocoas
                )

                return cocoaAnalysisRepository.getDiagnosisSession(newSessionId)
            }
        }
    }
}