package com.neotelemetrixgdscunand.kakaoxpert.data.local.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.neotelemetrixgdscunand.kakaoxpert.data.local.database.entity.CocoaAnalysisPreviewEntity
import com.neotelemetrixgdscunand.kakaoxpert.data.local.database.entity.relations.CocoaAnalysisPreviewRelation
import kotlinx.coroutines.flow.Flow

@Dao
interface CocoaAnalysisPreviewDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(cocoaAnalysisPreviewEntities: List<CocoaAnalysisPreviewEntity>)

    @Update
    suspend fun updateAll(cocoaAnalysisPreviewEntities: List<CocoaAnalysisPreviewEntity>)

    @Query("UPDATE cocoa_analysis_preview SET is_deleted = 1")
    suspend fun setAllIsDeletedToTrue()

    @Query("DELETE FROM cocoa_analysis_preview WHERE is_deleted = 1")
    suspend fun deleteAllIsDeleted()

    @Query("DELETE FROM cocoa_analysis_preview")
    suspend fun resetTableData()

    @Query("UPDATE cocoa_analysis_preview SET last_synced_time = :newLastSyncedTime")
    suspend fun updateAllLastSyncedTime(newLastSyncedTime: Long)

    @Query("UPDATE cocoa_analysis_preview SET last_synced_time = :newLastSyncedTime WHERE session_id in (:sessionIds)")
    suspend fun updateLastSyncedTime(sessionIds: List<Int>, newLastSyncedTime: Long)

    @Transaction
    @Query(
        """
        SELECT cocoa_analysis_preview.session_id as preview_id FROM cocoa_analysis_preview
        LEFT JOIN saved_cocoa_analysis ON
        cocoa_analysis_preview.session_id = saved_cocoa_analysis.session_id
        WHERE saved_cocoa_analysis.session_id IS NULL
        ORDER BY cocoa_analysis_preview.created_at ASC
        LIMIT 5
    """
    )
    suspend fun getFiveOldestWhichNotHaveDetailsYet(): List<Int>

    @Query(
        """
        SELECT * FROM
            (
                SELECT preview.session_id, 
                preview.created_at,
                preview.session_name, 
                preview.session_image_url AS session_image_url_or_path,
                preview.predicted_price, 
                preview.last_synced_time,
                (
                    SELECT EXISTS (
                        SELECT * FROM saved_cocoa_analysis AS saved
                        WHERE saved.session_id = preview.session_id
                        LIMIT 1
                    )
                ) 
                AS is_details_available_in_local_db
                FROM cocoa_analysis_preview AS preview
                WHERE preview.is_deleted = 0
                
                UNION
                
                SELECT unsaved.unsaved_session_id AS session_id,
                unsaved.created_at,
                unsaved.session_name,
                unsaved.session_image_path AS session_image_url_or_path,
                2100 AS predicted_price,
                0 AS last_synced_time,
                1 AS is_details_available_in_local_db
                FROM unsaved_cocoa_analysis AS unsaved
            ) AS final
        
        WHERE final.session_name LIKE '%' || :query || '%'
        ORDER BY final.created_at DESC
    """
    )
    fun getAllAsPagingSource(query: String): PagingSource<Int, CocoaAnalysisPreviewRelation>


    @Query(
        """
        SELECT * FROM
            (
                SELECT preview.session_id, 
                preview.created_at,
                preview.session_name, 
                preview.session_image_url AS session_image_url_or_path,
                preview.predicted_price, 
                preview.last_synced_time,
                (
                    SELECT EXISTS (
                        SELECT * FROM saved_cocoa_analysis AS saved
                        WHERE saved.session_id = preview.session_id
                        LIMIT 1
                    )
                ) 
                AS is_details_available_in_local_db
                FROM cocoa_analysis_preview AS preview
                WHERE preview.is_deleted = 0
                
                UNION
                
                SELECT unsaved.unsaved_session_id AS session_id,
                unsaved.created_at,
                unsaved.session_name,
                unsaved.session_image_path AS session_image_url_or_path,
                2100 AS predicted_price,
                0 AS last_synced_time,
                1 AS is_details_available_in_local_db
                FROM unsaved_cocoa_analysis AS unsaved
            ) AS final
        ORDER BY final.created_at DESC LIMIT 10
    """
    )
    fun getSome(): Flow<List<CocoaAnalysisPreviewRelation>>
    // Full outer-like merge via union


}