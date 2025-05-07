package com.neotelemetrixgdscunand.kamekapp.domain.model

import kotlin.random.Random

data class DetectedCacao(
    val id: Int = Random.nextInt(1000_000_001, 2000_000_000),
    val cacaoNumber: Short,
    val boundingBox: BoundingBox,
    val disease: CacaoDisease
)

fun getDetectedDiseaseCacaos(): List<DetectedCacao> {
    return listOf(
        DetectedCacao(
            cacaoNumber = 1,
            boundingBox = BoundingBox(
                0.1f,
                0.2f,
                0.3f,
                0.4f,
                cx = 1f,
                cy = 1f,
                h = 1f,
                w = 1f,
                cnf = 1f,
                cls = 1,
                label = "Kakao"
            ),
            disease = CacaoDisease.BLACKPOD
        ),
        DetectedCacao(
            cacaoNumber = 2,
            boundingBox = BoundingBox(
                0.5f,
                0.2f,
                0.7f,
                0.4f,
                cx = 1f,
                cy = 1f,
                h = 1f,
                w = 1f,
                cnf = 1f,
                cls = 1,
                label = "Kakao"
            ),
            disease = CacaoDisease.HELOPELTIS
        ),
        DetectedCacao(
            cacaoNumber = 3,
            boundingBox = BoundingBox(
                0.2f,
                0.3f,
                0.3f,
                0.4f,
                cx = 1f,
                cy = 1f,
                h = 1f,
                w = 1f,
                cnf = 1f,
                cls = 1,
                label = "Kakao"
            ),
            disease = CacaoDisease.BLACKPOD
        ),
        DetectedCacao(
            cacaoNumber = 4,
            boundingBox = BoundingBox(
                0.2f,
                0.3f,
                0.3f,
                0.4f,
                cx = 1f,
                cy = 1f,
                h = 1f,
                w = 1f,
                cnf = 1f,
                cls = 1,
                label = "Kakao"
            ),
            disease = CacaoDisease.BLACKPOD
        ),
        DetectedCacao(
            cacaoNumber = 5,
            boundingBox = BoundingBox(
                0.2f,
                0.3f,
                0.3f,
                0.4f,
                cx = 1f,
                cy = 1f,
                h = 1f,
                w = 1f,
                cnf = 1f,
                cls = 1,
                label = "Kakao"
            ),
            disease = CacaoDisease.BLACKPOD
        ),

        )
}