package com.neotelemetrixgdscunand.kakaoxpert.data.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

// bb = bounding box
@Entity(
    tableName = "unsaved_detected_cocoa",
    foreignKeys = [ForeignKey(
        entity = UnsavedCocoaAnalysisEntity::class,
        parentColumns = ["unsaved_session_id"],
        childColumns = ["unsaved_session_id"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["unsaved_session_id"])]
)
data class UnsavedDetectedCocoaEntity(
    @ColumnInfo("unsaved_id")
    @PrimaryKey(autoGenerate = true)
    val unsavedId: Int = 0,

    @ColumnInfo("unsaved_session_id")
    val unsavedSessionId: Int,

    @ColumnInfo("bb_label")
    val bbLabel: String,

    @ColumnInfo("damage_percentage")
    val damagePercentage: Float,

    @ColumnInfo("bb_coordinate_top")
    val bbCoordinateTop: Float,

    @ColumnInfo("bb_cls")
    val bbCls: Int,

    @ColumnInfo("bb_height")
    val bbHeight: Float,

    @ColumnInfo("bb_center_x")
    val bbCenterX: Float,

    @ColumnInfo("bb_center_y")
    val bbCenterY: Float,

    @ColumnInfo("bb_coordinate_bottom")
    val bbCoordinateBottom: Float,

    @ColumnInfo("variety_info_id")
    val varietyInfoId: Int,

    @ColumnInfo("cocoa_number")
    val cocoaNumber: Int,

    @ColumnInfo("disease_id")
    val diseaseId: Int,

    @ColumnInfo("bb_coordinate_left")
    val bbCoordinateLeft: Float,

    @ColumnInfo("bb_width")
    val bbWidth: Float,

    @ColumnInfo("bb_confidence")
    val bbConfidence: Float,

    @ColumnInfo("bb_coordinate_right")
    val bbCoordinateRight: Float,

    @ColumnInfo("damage_level")
    val damageLevel: Int
)