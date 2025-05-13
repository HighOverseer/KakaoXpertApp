package com.neotelemetrixgdscunand.kakaoxpert.data.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.neotelemetrixgdscunand.kakaoxpert.data.remote.dto.DetectedCocoaDto

@Entity(tableName = "unsaved_cocoa_analysis")
data class UnsavedCocoaAnalysisEntity(
    @ColumnInfo("unsaved_session_id")
    @PrimaryKey(autoGenerate = true)
    val unsavedSessionId: Int = 0,

    @ColumnInfo("created_at")
    val createdAt: Long,

    @ColumnInfo("detected_cocoas")
    val detectedCocoas: List<DetectedCocoaDto?>? = null,

    @ColumnInfo("session_name")
    val sessionName: String,

    @ColumnInfo("solution_en")
    val solutionEn: String,

    @ColumnInfo("user_id")
    val userId: Int,

    @ColumnInfo("session_image_path")
    val sessionImagePath: String,

    @ColumnInfo("prevention_id")
    val preventionId: String,

    @ColumnInfo("prevention_en")
    val preventionEn: String,

    @ColumnInfo("solution_id")
    val solutionId: String
)