package com.neotelemetrixgdscunand.kakaoxpert.data.local.database.entity.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.neotelemetrixgdscunand.kakaoxpert.data.local.database.entity.SavedCocoaAnalysisEntity
import com.neotelemetrixgdscunand.kakaoxpert.data.local.database.entity.SavedDetectedCocoaEntity

data class SavedCocoaAnalysisAndDetectedCocoas(
    @Embedded
    val savedCocoaAnalysisEntity: SavedCocoaAnalysisEntity,

    @Relation(
        parentColumn = "session_id",
        entityColumn = "session_id"
    )
    val savedDetectedCocoaEntities:List<SavedDetectedCocoaEntity>
)