package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.analysissessionresult.util

import android.content.Context
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize
import com.neotelemetrixgdscunand.kakaoxpert.R
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.BoundingBox
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.DetectedCocoa
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Grey63
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Grey67
import kotlinx.collections.immutable.ImmutableList

fun Modifier.shimmeringEffect(
    gradientShimmeringColor: ImmutableList<Color>
) = composed {
    var size by remember {
        mutableStateOf(IntSize.Zero)
    }

    val transition = rememberInfiniteTransition(label = "shimmering_transition")
    val startOffsetX by transition.animateFloat(
        initialValue = -2 * size.width.toFloat(),
        targetValue = 2 * size.width.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(1000)
        ),
        label = "shimmering_transition"
    )

    background(
        brush = Brush.linearGradient(
            colors = gradientShimmeringColor,
            start = Offset(startOffsetX, 0f),
            end = Offset(startOffsetX + size.width.toFloat(), size.height.toFloat())
        ),
    )

        .onGloballyPositioned {
            size = it.size
        }
}

fun Modifier.shimmeringEffect() = composed {
    var size by remember {
        mutableStateOf(IntSize.Zero)
    }

    val transition = rememberInfiniteTransition(label = "shimmering_transition")
    val startOffsetX by transition.animateFloat(
        initialValue = -2 * size.width.toFloat(),
        targetValue = 2 * size.width.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(1000)
        ),
        label = "shimmering_transition"
    )

    background(
        brush = Brush.linearGradient(
            colors = listOf(Grey67, Grey63, Grey67),
            start = Offset(startOffsetX, 0f),
            end = Offset(startOffsetX + size.width.toFloat(), size.height.toFloat())
        ),
    )

        .onGloballyPositioned {
            size = it.size
        }
}

fun formatSellPriceEstimation(context: Context, sellPrice: Float): String {
    val lowerBound = sellPrice.minus(100f).coerceAtLeast(0f)
    val upperBound = sellPrice.plus(100f).coerceAtMost(2000f)


    val lowerBoundString = formatBound(lowerBound)
    val upperBoundString = formatBound(upperBound)

    return context.getString(R.string.sekitar_buah, lowerBoundString, upperBoundString)
}

fun formatSellPriceEstimationForHistory(sellPrice: Float): String {
    val lowerBound = sellPrice.minus(100f).coerceAtLeast(0f)
    val upperBound = sellPrice.plus(100f).coerceAtMost(2000f)


    val lowerBoundString = formatBound(lowerBound)
    val upperBoundString = formatBound(upperBound)

    return "$lowerBoundString - $upperBoundString/buah"
}

fun formatBound(bound: Float): String {

    val indexFloatingPoint = bound.toString().indexOfFirst {
        it == '.'
    }
    var numberFloat = bound.toString().substring(indexFloatingPoint + 1, bound.toString().length)

    val boundWithoutFloat = bound
        .toString()
        .substring(0, indexFloatingPoint)

    if (numberFloat == "0") numberFloat = ""

    if (numberFloat.length > 2) numberFloat = numberFloat.substring(0, 2)

    return if (bound >= 1000) {
        val beforeDot = boundWithoutFloat
            .reversed()
            .substring(0, 3)
            .reversed()
        val afterDot = boundWithoutFloat
            .reversed()
            .substring(3, boundWithoutFloat.length)
            .reversed()

        StringBuilder().apply {
            append("Rp ")
            append(afterDot)
            append(".")
            append(beforeDot)
            if (numberFloat.isNotEmpty()) {
                append(",")
                append(numberFloat)
            }
        }.toString()
    } else {
        StringBuilder().apply {
            append("Rp ")
            append(boundWithoutFloat)
            if (numberFloat.isNotEmpty()) {
                append(",")
                append(numberFloat)
            }
        }.toString()
    }

}


fun Float.checkForZeroAfterFloatingPoint(): String {
    return if (this % 1.0 == 0.0) {
        this.toInt().toString()  // Convert to Int if there's no fractional part
    } else {
        this.toString()  // Keep as Double if there is a fractional part
    }
}

fun String.checkForZeroAfterFloatingPoint(): String {
    return if (this.toFloat() % 1.0 == 0.0) {
        this.toFloat().toInt().toString()  // Convert to Int if there's no fractional part
    } else {
        this  // Keep as Double if there is a fractional part
    }
}


fun formatDamageLevelEstimation(context: Context, damageLevel: Float): String {
    val level = when {
        damageLevel == 0f -> context.getString(R.string.tidak_ada)
        damageLevel > 0 && damageLevel <= 0.3 -> context.getString(R.string.Light)
        damageLevel > 0.3 && damageLevel <= 0.6 -> context.getString(R.string.sedang)
        else -> context.getString(R.string.berat)
    }

    val lowerBound = damageLevel.minus(0.05f).coerceAtLeast(0f) * 100
    val upperBound = damageLevel.plus(0.05f).coerceAtMost(1f) * 100

    return context.getString(
        R.string.buah_rusak,
        level,
        lowerBound.roundOffDecimal(2).checkForZeroAfterFloatingPoint(),
        upperBound.roundOffDecimal(2).checkForZeroAfterFloatingPoint()
    )
}

fun Float.roundOffDecimal(n: Int = 3): Float {
    val rounder = (10 * n).toFloat()
    return Math.round(this * rounder) / rounder
}

fun DetectedCocoa.getBoundingBoxWithItsNameAsTheLabel(
    context: Context
): BoundingBox {
    return boundingBox.run {
        BoundingBox(
            x1,
            y1,
            x2,
            y2,
            cx,
            cy,
            w,
            h,
            cnf,
            cls,
            context.getString(R.string.kakao, cacaoNumber.toString())
        )
    }
}