package com.neotelemetrixgdscunand.kakaoxpert.data.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(
    tableName = "saved_cocoa_analysis",
    foreignKeys = [ForeignKey(
        entity = CocoaAnalysisPreviewEntity::class,
        parentColumns = ["session_id"],
        childColumns = ["session_id"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["session_id"])]
)
data class SavedCocoaAnalysisEntity(
    @ColumnInfo("session_id")
    @PrimaryKey(autoGenerate = false)
    val sessionId: Int,

    @ColumnInfo("created_at")
    val createdAt: Long,

    @ColumnInfo("session_name")
    val sessionName: String,

    @ColumnInfo("solution_en")
    val solutionEn: String,


//    @ColumnInfo("user_id")
//    val userId: Int,

    @ColumnInfo("session_image_url")
    val sessionImageUrl: String,

    @ColumnInfo("preventions_id")
    val preventionsId: String,

    @ColumnInfo("preventions_en")
    val preventionsEn: String,

    @ColumnInfo("solution_id")
    val solutionId: String,

    @ColumnInfo("total_predicted_price")
    val totalPredictedPrice: Float,

    @ColumnInfo("is_solutions_from_llm")
    val isSolutionsFromLLm: Boolean,
)