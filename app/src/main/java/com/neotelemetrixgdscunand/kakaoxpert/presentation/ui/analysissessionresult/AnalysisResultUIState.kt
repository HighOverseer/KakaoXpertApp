package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.analysissessionresult

import com.neotelemetrixgdscunand.kakaoxpert.domain.model.BoundingBox
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.CocoaDisease
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.DetectedCocoa
import com.neotelemetrixgdscunand.kakaoxpert.presentation.dui.DiagnosisResultOverviewDui
import com.neotelemetrixgdscunand.kakaoxpert.presentation.dui.PriceAnalysisOverviewDui
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableMap


val emptyGroupedMap = emptyMap<CocoaDisease, ImmutableList<DetectedCocoa>>().toImmutableMap()
val emptyGroupedMap2 = emptyMap<CocoaDisease, ImmutableMap<Int, ImmutableList<DetectedCocoa>>>().toImmutableMap()

data class AnalysisResultUIState(
    val isLoading: Boolean = false,
    //val diagnosisSession: AnalysisSessionDui = AnalysisSessionDui(),
    val sessionId:Int = -1,
    val sessionTitle:String = "",
    val groupedDetectedDiseaseToDetectedCocoas:ImmutableMap<CocoaDisease, ImmutableList<DetectedCocoa>> = emptyGroupedMap,
    val groupedDetectedDiseaseToDamageLevelsToDetectedCocoas:ImmutableMap<CocoaDisease, ImmutableMap<Int, ImmutableList<DetectedCocoa>>> = emptyGroupedMap2,
    val priceAnalysisOverview: PriceAnalysisOverviewDui = PriceAnalysisOverviewDui(),
    val diagnosisResultOverview: DiagnosisResultOverviewDui = DiagnosisResultOverviewDui(),
    val imageBoundingBoxes:ImmutableList<BoundingBox> = persistentListOf(),
    val imagePreviewPath: String? = null,
    val isDiagnosisTabSelected:Boolean = true,
)