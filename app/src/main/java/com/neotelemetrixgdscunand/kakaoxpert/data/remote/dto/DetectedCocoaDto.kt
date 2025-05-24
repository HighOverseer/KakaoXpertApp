package com.neotelemetrixgdscunand.kakaoxpert.data.remote.dto

import com.google.gson.annotations.SerializedName

data class DetectedCocoaDto(

    @field:SerializedName("bb_label")
    val bbLabel: String? = null,

    @field:SerializedName("damage_level")
    val damageLevel: Float? = null,

    @field:SerializedName("predicted_price")
    val predictedPrice: Float? = null,

    @field:SerializedName("bb_coordinate_top")
    val bbCoordinateTop: Float? = null,

    @field:SerializedName("bb_cls")
    val bbCls: Int? = null,

    @field:SerializedName("bb_height")
    val bbHeight: Float? = null,

    @field:SerializedName("session_id")
    val sessionId: Int? = null,

    @field:SerializedName("bb_center_x")
    val bbCenterX: Float? = null,

    @field:SerializedName("bb_center_y")
    val bbCenterY: Float? = null,

    @field:SerializedName("bb_coordinate_bottom")
    val bbCoordinateBottom: Float? = null,

    @field:SerializedName("variety_info_id")
    val varietyInfoId: Int? = null,

    @field:SerializedName("cocoa_number")
    val cocoaNumber: Int? = null,

    @field:SerializedName("disease_id")
    val diseaseId: Int? = null,

    @field:SerializedName("bb_coordinate_left")
    val bbCoordinateLeft: Float? = null,

    @field:SerializedName("bb_width")
    val bbWidth: Float? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("bb_confidence")
    val bbConfidence: Float? = null,

    @field:SerializedName("bb_coordinate_right")
    val bbCoordinateRight: Float? = null,
)
