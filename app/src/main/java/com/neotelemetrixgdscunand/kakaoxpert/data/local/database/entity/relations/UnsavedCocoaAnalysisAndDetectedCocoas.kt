package com.neotelemetrixgdscunand.kakaoxpert.data.local.database.entity.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.neotelemetrixgdscunand.kakaoxpert.data.local.database.entity.UnsavedCocoaAnalysisEntity
import com.neotelemetrixgdscunand.kakaoxpert.data.local.database.entity.UnsavedDetectedCocoaEntity

data class UnsavedCocoaAnalysisAndDetectedCocoas(
    @Embedded
    val unsavedCocoaAnalysisEntity: UnsavedCocoaAnalysisEntity,

    @Relation(
        parentColumn = "unsaved_session_id",
        entityColumn = "unsaved_session_id"
    )
    val unsavedDetectedCocoaEntities: List<UnsavedDetectedCocoaEntity>
)