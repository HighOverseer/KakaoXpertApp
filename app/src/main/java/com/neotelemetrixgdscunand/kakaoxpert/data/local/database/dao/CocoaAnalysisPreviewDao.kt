package com.neotelemetrixgdscunand.kakaoxpert.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.neotelemetrixgdscunand.kakaoxpert.data.local.database.entity.CocoaAnalysisPreviewEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CocoaAnalysisPreviewDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(cocoaAnalysisPreviewEntities: List<CocoaAnalysisPreviewEntity>)

    @Query("UPDATE cocoa_analysis_preview SET is_deleted = 1")
    suspend fun setAllIsDeletedToTrue()

    @Query("DELETE FROM cocoa_analysis_preview WHERE is_deleted = 1")
    suspend fun deleteAllIsDeleted()

    @Query("SELECT * FROM cocoa_analysis_preview WHERE is_deleted = 0")
    fun getAll(): Flow<CocoaAnalysisPreviewEntity>
}