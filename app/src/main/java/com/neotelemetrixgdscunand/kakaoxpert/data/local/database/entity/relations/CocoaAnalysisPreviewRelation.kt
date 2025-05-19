package com.neotelemetrixgdscunand.kakaoxpert.data.local.database.entity.relations

import androidx.room.ColumnInfo

data class CocoaAnalysisPreviewRelation(
    @ColumnInfo("session_id")
    val sessionId: Int,

    @ColumnInfo("created_at")
    val createdAt: Long,

    @ColumnInfo("session_name")
    val sessionName: String,

    @ColumnInfo("session_image_url_or_path")
    val sessionImageUrlOrPath: String,

    @ColumnInfo("predicted_price")
    val predictedPrice: Float = 2100f,

    @ColumnInfo("last_synced_time")
    val lastSyncedTime: Long,

    @ColumnInfo("is_details_available_in_local_db")
    val isDetailAvailableInLocalDB: Boolean = false,
)