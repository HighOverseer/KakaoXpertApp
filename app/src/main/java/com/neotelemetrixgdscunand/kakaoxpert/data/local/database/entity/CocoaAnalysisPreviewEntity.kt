package com.neotelemetrixgdscunand.kakaoxpert.data.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cocoa_analysis_preview")
data class CocoaAnalysisPreviewEntity(
    @ColumnInfo("session_id")
    @PrimaryKey(autoGenerate = false)
    val sessionId: Int,

    @ColumnInfo("created_at")
    val createdAt: Long,

    @ColumnInfo("session_name")
    val sessionName: String,

    @ColumnInfo("session_image_url")
    val sessionImageUrl: String,

    @ColumnInfo("predicted_price")
    val predictedPrice:Long = 2100L,

    @ColumnInfo("is_deleted")
    val isDeleted: Boolean = false,
)