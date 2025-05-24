package com.neotelemetrixgdscunand.kakaoxpert.domain.model

import kotlin.random.Random

data class DetectedCocoa(
    val id: Int = Random.nextInt(1000_000_001, 2000_000_000),
    val cacaoNumber: Short,
    val boundingBox: BoundingBox,
    val disease: CocoaDisease,
    val predictedPriceInIdr:Float,
    val damageLevel:Float
)

fun getDetectedDiseaseCacaos(): List<DetectedCocoa> {
    return listOf(
        DetectedCocoa(
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
            disease = CocoaDisease.BLACKPOD,
            predictedPriceInIdr = 1680f,
            damageLevel = 0.5f
        ),
        DetectedCocoa(
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
            disease = CocoaDisease.HELOPELTIS,
            predictedPriceInIdr = 1680f,
            damageLevel = 0.5f
        ),
        DetectedCocoa(
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
            disease = CocoaDisease.BLACKPOD,
            predictedPriceInIdr = 1680f,
            damageLevel = 0.5f
        ),
        DetectedCocoa(
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
            disease = CocoaDisease.BLACKPOD,
            predictedPriceInIdr = 1680f,
            damageLevel = 0.5f
        ),
        DetectedCocoa(
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
            disease = CocoaDisease.BLACKPOD,
            predictedPriceInIdr = 1680f,
            damageLevel = 0.5f
        ),

        )
}