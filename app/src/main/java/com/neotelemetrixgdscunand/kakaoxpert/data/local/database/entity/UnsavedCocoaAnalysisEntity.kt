package com.neotelemetrixgdscunand.kakaoxpert.data.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "unsaved_cocoa_analysis")
data class UnsavedCocoaAnalysisEntity(
    @ColumnInfo("unsaved_session_id")
    @PrimaryKey(autoGenerate = false)
    val unsavedSessionId: Int = System.currentTimeMillis().toInt(),

    @ColumnInfo("created_at")
    val createdAt: Long,

    @ColumnInfo("session_name")
    val sessionName: String,

    @ColumnInfo("total_predicted_price")
    val totalPredictedPrice: Float,

//    @ColumnInfo("solution_en")
//    val solutionEn: String,

//    @ColumnInfo("user_id")
//    val userId: Int,

    @ColumnInfo("session_image_path")
    val sessionImagePath: String,

    @ColumnInfo("is_solutions_from_llm")
    val isSolutionsFromLLm: Boolean,

//    @ColumnInfo("prevention_id")
//    val preventionId: String,
//
//    @ColumnInfo("prevention_en")
//    val preventionEn: String,
//
//    @ColumnInfo("solution_id")
//    val solutionId: String
)