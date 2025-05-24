package com.neotelemetrixgdscunand.kakaoxpert.data.local.database

import androidx.sqlite.db.SimpleSQLiteQuery
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.SearchAnalysisHistoryCategory
import java.util.Calendar

object QueryUtil {

    fun getSearchAnalysisPreviewQuery(
        searchQuery: String,
        category: SearchAnalysisHistoryCategory
    ): SimpleSQLiteQuery {
        val query = StringBuilder().append(
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
                unsaved.total_predicted_price AS predicted_price,
                0 AS last_synced_time,
                1 AS is_details_available_in_local_db
                FROM unsaved_cocoa_analysis AS unsaved
            ) AS final
    """
        )

        val arguments = mutableListOf<Any>()
        query.append(" WHERE final.session_name LIKE '%' || ? || '%'")
        arguments.add(searchQuery)

        val timeThreshold = when (category) {
            SearchAnalysisHistoryCategory.TODAY -> {
                val calendar = Calendar.getInstance()
                calendar[Calendar.HOUR_OF_DAY] = 0
                calendar[Calendar.MINUTE] = 0
                calendar[Calendar.SECOND] = 0
                calendar[Calendar.MILLISECOND] = 0

                val todayTimeThreshold = calendar.timeInMillis
                todayTimeThreshold
            }

            SearchAnalysisHistoryCategory.WEEK -> {
                val calendar = Calendar.getInstance()
                calendar[Calendar.DAY_OF_WEEK] = Calendar.MONDAY
                calendar[Calendar.HOUR_OF_DAY] = 0
                calendar[Calendar.MINUTE] = 0
                calendar[Calendar.SECOND] = 0
                calendar[Calendar.MILLISECOND] = 0

                val weekTimeThreshold = calendar.timeInMillis
                weekTimeThreshold
            }

            SearchAnalysisHistoryCategory.MONTH -> {
                val calendar = Calendar.getInstance()
                calendar[Calendar.DAY_OF_MONTH] = 1
                calendar[Calendar.HOUR_OF_DAY] = 0
                calendar[Calendar.MINUTE] = 0
                calendar[Calendar.SECOND] = 0
                calendar[Calendar.MILLISECOND] = 0

                val monthTimeThreshold = calendar.timeInMillis
                monthTimeThreshold
            }

            SearchAnalysisHistoryCategory.ALL -> {
                //No Time Filter Needed
                null
            }
        }

        timeThreshold?.let {
            query.append(" AND final.created_at >= ?")
            arguments.add(timeThreshold)
        }

        query.append(" ORDER BY final.created_at DESC")

        return SimpleSQLiteQuery(query.toString(), arguments.toTypedArray())
    }
}