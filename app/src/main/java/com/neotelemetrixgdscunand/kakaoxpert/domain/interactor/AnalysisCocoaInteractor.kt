package com.neotelemetrixgdscunand.kakaoxpert.domain.interactor

import com.neotelemetrixgdscunand.kakaoxpert.domain.common.CocoaAnalysisError
import com.neotelemetrixgdscunand.kakaoxpert.domain.common.Result
import com.neotelemetrixgdscunand.kakaoxpert.domain.presentation.CocoaImageDetectorHelper
import com.neotelemetrixgdscunand.kakaoxpert.domain.presentation.ImageDetectorResult
import com.neotelemetrixgdscunand.kakaoxpert.domain.usecase.AnalysisCocoaUseCase
import com.neotelemetrixgdscunand.kakaoxpert.domain.data.CocoaAnalysisRepository
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.CocoaDisease
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.DetectedCocoa
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.AnalysisSession
import kotlinx.coroutines.ensureActive
import kotlin.coroutines.coroutineContext

class AnalysisCocoaInteractor(
    private val cacaoImageDetectorHelper: CocoaImageDetectorHelper,
    private val cocoaAnalysisRepository: CocoaAnalysisRepository
):AnalysisCocoaUseCase {

    override suspend fun invoke(
        sessionName:String,
        imagePath: String
    ): Result<AnalysisSession, CocoaAnalysisError> {
        val result = try {
            cacaoImageDetectorHelper.detect(imagePath)
        }finally {
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

                coroutineContext.ensureActive()

                val newSessionId = cocoaAnalysisRepository.saveDiagnosis(
                    sessionName = sessionName,
                    imageOrUrlPath = imagePath,
                    predictedPrice = 1680f,
                    detectedCocoas = detectedCocoas
                )

               val newSavedAnalysis = cocoaAnalysisRepository.getDiagnosisSession(newSessionId)

                return Result.Success(newSavedAnalysis)
            }
        }
    }
}