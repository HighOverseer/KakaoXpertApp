package com.neotelemetrixgdscunand.kakaoxpert.data.local.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.neotelemetrixgdscunand.kakaoxpert.data.local.database.entity.relations.UnsavedCocoaAnalysisAndDetectedCocoasRelation

@Dao
interface UnsavedCocoaAnalysisDao {

    @Transaction
    @Query("SELECT * FROM unsaved_cocoa_analysis ORDER BY created_at ASC LIMIT 5")
    suspend fun getFiveOldestData(): List<UnsavedCocoaAnalysisAndDetectedCocoasRelation>

    @Query("DELETE FROM unsaved_cocoa_analysis WHERE unsaved_session_id IN (:ids)")
    suspend fun deleteAllByIds(ids: List<Int>)


}