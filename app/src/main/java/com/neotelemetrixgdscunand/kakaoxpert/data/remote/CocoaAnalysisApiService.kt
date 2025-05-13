package com.neotelemetrixgdscunand.kakaoxpert.data.remote

import com.neotelemetrixgdscunand.kakaoxpert.data.remote.dto.AnalysisSessionDto
import com.neotelemetrixgdscunand.kakaoxpert.data.remote.dto.AnalysisSessionPreviewDto
import com.neotelemetrixgdscunand.kakaoxpert.data.remote.dto.Response
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.AnalysisSession
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.DetectedCocoa
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PartMap
import retrofit2.http.Path
import java.io.File

interface CocoaAnalysisApiService {
    @GET("analysis-history")
    suspend fun getAllAnalysisSessionPreviews(): Response<List<AnalysisSessionPreviewDto>>

    @GET("analysis-history/{id}")
    suspend fun getAnalysisSessionById(
        @Path("id") sessionId:Int
    ): Response<AnalysisSessionDto>

    @Multipart
    @POST("analysis-history")
    suspend fun addNewAnalysisHistory(
        @Part sessionImage: MultipartBody.Part,

        @Part("session_name") sessionName: RequestBody,
        @Part("detected_diseases_ids") detectedDiseaseIds: RequestBody,
        @Part("variety_ids") varietyIds: RequestBody,
        @Part("damage_percentage") damagePercentage: RequestBody,
        @Part("cocoa_numbers") cocoaNumbers: RequestBody,

        @Part("bb_coordinates_left") bbCoordinatesLeft: RequestBody,
        @Part("bb_coordinates_top") bbCoordinatesTop: RequestBody,
        @Part("bb_coordinates_right") bbCoordinatesRight: RequestBody,
        @Part("bb_coordinates_bottom") bbCoordinatesBottom: RequestBody,

        @Part("bb_widths") bbWidths: RequestBody,
        @Part("bb_heights") bbHeights: RequestBody,
        @Part("bb_centers_x") bbCentersX: RequestBody,
        @Part("bb_centers_y") bbCentersY: RequestBody,

        @Part("bb_confidences") bbConfidences: RequestBody,
        @Part("bb_cls") bbCls: RequestBody,
        @Part("bb_labels") bbLabels: RequestBody
    ): Response<AnalysisSessionDto>

    class PostRequestBody private constructor(
        val sessionImage: MultipartBody.Part,
        val sessionName: RequestBody,
        val detectedDiseaseIds: RequestBody,
        val varietyIds: RequestBody,
        val damagePercentage: RequestBody,
        val cocoaNumbers: RequestBody,

        val bbCoordinatesLeft: RequestBody,
        val bbCoordinatesTop: RequestBody,
        val bbCoordinatesRight: RequestBody,
        val bbCoordinatesBottom: RequestBody,

        val bbWidths: RequestBody,
        val bbHeights: RequestBody,
        val bbCentersX: RequestBody,
        val bbCentersY: RequestBody,

        val bbConfidences: RequestBody,
        val bbCls: RequestBody,
        val bbLabels: RequestBody
    ){

        companion object{
            fun createPostRequestBody(
                sessionImagePath:String,
                sessionName:String,
                detectedCocoas: List<DetectedCocoa>,
            ):PostRequestBody{
                val sessionImageFile = File(sessionImagePath)
                if(!sessionImageFile.exists()) throw Exception("error")

                val sessionImagePart = sessionImageFile.asImageRequestBody()
                    .run {
                        MultipartBody.Part.createFormData(
                            "session_image",
                            sessionImageFile.name,
                            this
                        )
                    }


                val detectedDiseaseIds = detectedCocoas
                    .map { it.disease.id.toString() }
                    .toTextRequestBody()

                val varietyIds = detectedCocoas.map { "1" }
                    .toTextRequestBody()

                val damagePercentages = detectedCocoas.map { "0.1" }
                    .toTextRequestBody()

                val cocoaNumbers = detectedCocoas.map {
                    it.cacaoNumber.toString()
                }.toTextRequestBody()

                val bbCoordinatesLeft = detectedCocoas.map {
                    it.boundingBox.x1.toString()
                }.toTextRequestBody()

                val bbCoordinatesTop = detectedCocoas.map {
                    it.boundingBox.y1.toString()
                }.toTextRequestBody()

                val bbCoordinatesRight = detectedCocoas.map {
                    it.boundingBox.x2.toString()
                }.toTextRequestBody()

                val bbCoordinatesBottom = detectedCocoas.map {
                    it.boundingBox.y2.toString()
                }.toTextRequestBody()

                val bbWidths = detectedCocoas.map {
                    it.boundingBox.w.toString()
                }.toTextRequestBody()

                val bbHeights = detectedCocoas.map {
                    it.boundingBox.h.toString()
                }.toTextRequestBody()

                val bbCentersX = detectedCocoas.map {
                    it.boundingBox.cx.toString()
                }.toTextRequestBody()

                val bbCentersY = detectedCocoas.map {
                    it.boundingBox.cy.toString()
                }.toTextRequestBody()

                val bbConfidences = detectedCocoas.map {
                    it.boundingBox.cnf.toString()
                }.toTextRequestBody()

                val bbCls = detectedCocoas.map {
                    it.boundingBox.cls.toString()
                }.toTextRequestBody()

                val bbLabels = detectedCocoas.map {
                    it.boundingBox.label
                }.toTextRequestBody()

                return PostRequestBody(
                    sessionImage = sessionImagePart,
                    sessionName = sessionName.toTextRequestBody(),
                    detectedDiseaseIds = detectedDiseaseIds,
                    varietyIds = varietyIds,
                    damagePercentage = damagePercentages,
                    cocoaNumbers = cocoaNumbers,
                    bbCoordinatesLeft = bbCoordinatesLeft,
                    bbCoordinatesTop = bbCoordinatesTop,
                    bbCoordinatesRight = bbCoordinatesRight,
                    bbCoordinatesBottom = bbCoordinatesBottom,
                    bbWidths = bbWidths,
                    bbHeights = bbHeights,
                    bbCentersX = bbCentersX,
                    bbCentersY = bbCentersY,
                    bbConfidences = bbConfidences,
                    bbCls = bbCls,
                    bbLabels = bbLabels
                )
            }
        }
    }

}


