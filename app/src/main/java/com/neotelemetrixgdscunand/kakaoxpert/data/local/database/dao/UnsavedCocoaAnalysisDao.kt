package com.neotelemetrixgdscunand.kakaoxpert.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.neotelemetrixgdscunand.kakaoxpert.data.local.database.entity.UnsavedCocoaAnalysisEntity
import com.neotelemetrixgdscunand.kakaoxpert.data.local.database.entity.relations.UnsavedCocoaAnalysisAndDetectedCocoasRelation

@Dao
interface UnsavedCocoaAnalysisDao {

    @Insert
    suspend fun insert(unsavedCocoaAnalysisEntity: UnsavedCocoaAnalysisEntity): Long

    @Transaction
    @Query("SELECT * FROM unsaved_cocoa_analysis ORDER BY created_at ASC LIMIT 5")
    suspend fun getFiveOldestData(): List<UnsavedCocoaAnalysisAndDetectedCocoasRelation>

    @Query("DELETE FROM unsaved_cocoa_analysis WHERE unsaved_session_id IN (:ids)")
    suspend fun deleteAllByIds(ids: List<Int>)

    @Query("SELECT * FROM unsaved_cocoa_analysis WHERE unsaved_session_id = :sessionId")
    suspend fun getById(sessionId: Int): UnsavedCocoaAnalysisAndDetectedCocoasRelation?

    @Query("DELETE FROM unsaved_cocoa_analysis")
    suspend fun resetTableData()
}