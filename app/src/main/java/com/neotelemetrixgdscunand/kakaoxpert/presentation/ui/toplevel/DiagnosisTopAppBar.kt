package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.toplevel

import android.content.res.Configuration
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.AnimationConstants.DefaultDurationMillis
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.neotelemetrixgdscunand.kakaoxpert.R
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Black10
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Grey90
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.KakaoXpertTheme
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.toplevel.diagnosishistory.component.TakePhotoSection

private const val topAppBarSubComposeId = "topAppBar"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiagnosisTopAppBar(
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior,
    isVisibleProvider: () -> Boolean = { false },
    visibilityAnimationDurationMillis: Int = DefaultDurationMillis,
    onClick: () -> Unit = {}
) {
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current

    val initialHeightDp = remember {
        val screenHeightDp = configuration.screenHeightDp.dp
        val initialValueDp = when (configuration.orientation) {
            Configuration.ORIENTATION_PORTRAIT -> screenHeightDp / 2
            else -> screenHeightDp
        }
        initialValueDp
    }
    var expandedHeightPx by remember {
        val initialValuePx = with(density) { initialHeightDp.toPx() }
        mutableIntStateOf(initialValuePx.toInt())
    }

    val expandedHeightDp = remember(expandedHeightPx) {
        with(density) { expandedHeightPx.toDp() }
    }

    val animatedDp by animateDpAsState(
        targetValue = expandedHeightDp,
        animationSpec = spring(
            stiffness = Spring.StiffnessMediumLow
        )
    )

    Crossfade(
        isVisibleProvider(),
        animationSpec = tween(visibilityAnimationDurationMillis)
    ) { it ->
        if (it) {
            TopAppBar(
                modifier = modifier,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Grey90,
                    scrolledContainerColor = Grey90
                ),
                expandedHeight = animatedDp,
                title = {},
                scrollBehavior = scrollBehavior,
                actions = {
                    ContentTopAppBar(
                        onTopAppBarHeightMeasured = {
                            expandedHeightPx = it
                        },
                        navigateToTakePhoto = onClick
                    )
                }
            )
        }
    }
}

@Composable
fun ContentTopAppBar(
    modifier: Modifier = Modifier,
    onTopAppBarHeightMeasured: (Int) -> Unit = {},
    navigateToTakePhoto: () -> Unit = {},
) {
    val configuration = LocalConfiguration.current
    SubcomposeLayout { constraints ->
        val contentMeasurable = subcompose(topAppBarSubComposeId) {
            val screenHeightDp = configuration.screenHeightDp.dp

            val topMarginRatio = 0.039f
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Spacer(Modifier.height(topMarginRatio * screenHeightDp))

                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = stringResource(R.string.diagnosis),
                    style = MaterialTheme.typography.headlineSmall,
                    color = Black10
                )

                val firstCardToTitleRatio = 0.037f
                Spacer(Modifier.height(screenHeightDp * firstCardToTitleRatio))

                TakePhotoSection(onClick = navigateToTakePhoto)

                Spacer(Modifier.height(16.dp))
            }
        }
        val placeables = contentMeasurable.map {
            it.measure(constraints)
        }

        val height = placeables.sumOf { it.height }

        onTopAppBarHeightMeasured(height)

        layout(constraints.maxWidth, height) {
            placeables.forEach {
                it.placeRelative(0, 0)
            }
        }
    }
}

@Preview
@Composable
private fun DiagnosisTopAppBarPreview() {
    KakaoXpertTheme {
        ContentTopAppBar()
    }
}