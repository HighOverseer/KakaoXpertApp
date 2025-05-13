package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.onboarding

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.neotelemetrixgdscunand.kakaoxpert.R
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Grey45
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Grey71
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.KakaoXpertTheme
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Orange90
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.auth.component.PrimaryButton
import com.neotelemetrixgdscunand.kakaoxpert.presentation.utils.ImagePainterStable
import com.neotelemetrixgdscunand.kakaoxpert.presentation.utils.collectChannelWhenStarted
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlin.math.roundToInt


@Composable
fun OnBoardingScreen(
    modifier: Modifier = Modifier,
    navigateUp: () -> Unit,
    viewModel: OnBoardingViewModel = hiltViewModel(),
    navigateToMainPage: () -> Unit,
    showSnackbar: (String) -> Unit
) {

    val lifecycle = LocalLifecycleOwner.current

    val context = LocalContext.current
    LaunchedEffect(true) {
        lifecycle.collectChannelWhenStarted(viewModel.uiEvent) { event ->
            when (event) {
                OnBoardingUIEvent.OnBoardingSessionFinished -> navigateToMainPage()
                is OnBoardingUIEvent.OnFailedFinishingSession -> {
                    showSnackbar(event.errorUIText.getValue(context))
                }
            }

        }
    }

    val isButtonEnabled by viewModel.isButtonEnabled.collectAsStateWithLifecycle()

    OnBoardingContent(
        modifier = modifier,
        navigateUp = navigateUp,
        onUserHasPressedStartButton = viewModel::onUserPressedStartButton,
        isButtonEnabledProvider = { isButtonEnabled }
    )
}

@Composable
fun OnBoardingContent(
    modifier: Modifier = Modifier,
    navigateUp: () -> Unit = { },
    onUserHasPressedStartButton: () -> Unit = {},
    isButtonEnabledProvider: () -> Boolean = { true }
) {
    var selectedTabIndex by rememberSaveable {
        mutableIntStateOf(0)
    }

    val constraints = rememberConstraintSet()

    val configuration = LocalConfiguration.current
    val screenHeightDp =
        configuration.screenHeightDp.dp
    val screenWidthDp =
        configuration.screenWidthDp.dp

    val birdLeftCloudYOffset = remember {
        Animatable(0f)
    }

    val birdLeftParentXOffset = remember {
        Animatable(0f)
    }

    val leftCloudParentXOffset = remember {
        Animatable(0f)
    }

    val rightCloudParentXOffset = remember {
        Animatable(0f)
    }

    val density = LocalDensity.current
    LaunchedEffect(selectedTabIndex) {
        with(density) {
            val targetBirdToLeftCloudMargin =
                getBirdToLeftCloudMargin(selectedTabIndex, screenHeightDp).toPx()
            val targetBirdToParentMargin =
                getBirdToParentMargin(selectedTabIndex, screenWidthDp).toPx()
            val targetLeftCloudToParentMargin =
                getLeftCloudToParentMargin(selectedTabIndex, screenWidthDp).toPx()
            val targetRightCloudToParentMargin =
                getRightCloudToParentMargin(selectedTabIndex, screenWidthDp).toPx()

            val animationSpec = spring<Float>(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )

            val birdLeftCloudAnim =
                async { birdLeftCloudYOffset.animateTo(targetBirdToLeftCloudMargin, animationSpec) }
            val birdToParentAnim =
                async { birdLeftParentXOffset.animateTo(targetBirdToParentMargin, animationSpec) }
            val leftCloudToParentAnim = async {
                leftCloudParentXOffset.animateTo(
                    targetLeftCloudToParentMargin,
                    animationSpec
                )
            }
            val rightCloudToParentAnim = async {
                rightCloudParentXOffset.animateTo(
                    targetRightCloudToParentMargin,
                    animationSpec
                )
            }

            awaitAll(
                birdLeftCloudAnim,
                birdToParentAnim,
                leftCloudToParentAnim,
                rightCloudToParentAnim
            )
        }
    }

    val onBoardingImageResId = remember(selectedTabIndex) {
        when (selectedTabIndex) {
            0 -> R.drawable.onboarding_1
            1 -> R.drawable.onboarding_2
            else -> R.drawable.onboarding_3
        }
    }

    val headlineResId = remember(selectedTabIndex) {
        when (selectedTabIndex) {
            0 -> R.string.onboarding_1
            1 -> R.string.onboarding_2
            else -> R.string.onboarding_3
        }
    }

    val descResId = remember(selectedTabIndex) {
        when (selectedTabIndex) {
            0 -> R.string.onboarding_1_desc
            1 -> R.string.onboarding_desc_2
            else -> R.string.onboarding_desc_3
        }
    }

    BackHandler(
        enabled = true
    ) {
        if (selectedTabIndex > 0) {
            selectedTabIndex--
        } else navigateUp()
    }

    ConstraintLayout(
        modifier = modifier
            .fillMaxSize(),
        constraintSet = constraints
    ) {
        val configuration = LocalConfiguration.current
        val onBoardingImageAspectRatio = 1.88f
        val leftCloudWidthRatio = 0.55f
        val leftCloudHeightRatio = 0.1138f
        val rightCloudWidthRatio = 0.40f
        val rightCloudHeightRatio = 0.0687f
        val birdAspectRatio = 5.02f
        val birdWidthToParentRatio = 0.2f
        val birdWidth = remember {
            (configuration.screenWidthDp * birdWidthToParentRatio).dp
        }
        val buttonWidthMin = remember {
            (configuration.screenWidthDp * 0.456f).dp
        }

        ImagePainterStable(
            modifier = Modifier
                .width(birdWidth)
                .aspectRatio(birdAspectRatio)
                .offset {
                    IntOffset(
                        x = birdLeftParentXOffset.value.roundToInt(),
                        y = -birdLeftCloudYOffset.value.roundToInt()
                    )
                }
                .layoutId(LayoutUtil.BIRD_ID),
            drawableResId = R.drawable.bird,
            contentScale = ContentScale.Crop,
            contentDescription = null
        )

        ImagePainterStable(
            modifier = Modifier
                .fillMaxHeight(rightCloudHeightRatio)
                .fillMaxWidth(rightCloudWidthRatio)
                .layoutId(LayoutUtil.RIGHT_CLOUD_ID)
                .offset {
                    IntOffset(
                        x = -rightCloudParentXOffset.value.roundToInt(),
                        y = 0
                    )

                },
            drawableResId = R.drawable.right_cloud,
            contentDescription = null,
            contentScale = ContentScale.Crop
        )


        ImagePainterStable(
            modifier = Modifier
                .fillMaxHeight(leftCloudHeightRatio)
                .fillMaxWidth(leftCloudWidthRatio)
                .layoutId(LayoutUtil.LEFT_CLOUD_ID)
                .offset {
                    IntOffset(
                        x = leftCloudParentXOffset.value.roundToInt(),
                        y = 0
                    )
                },
            drawableResId = R.drawable.left_cloud,
            contentDescription = null,
            contentScale = ContentScale.Crop
        )

        val headlineResIdModifier = remember(selectedTabIndex) {
            Modifier
                .fillMaxWidth()
                .layoutId(LayoutUtil.HEADLINE_ID)
        }
        Text(
            modifier = headlineResIdModifier,
            textAlign = TextAlign.Center,
            text = stringResource(headlineResId),
            style = MaterialTheme.typography.headlineLarge,
            color = Color.Black
        )


        val animatedContentModifier = remember {
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .aspectRatio(onBoardingImageAspectRatio)
                .layoutId(LayoutUtil.ON_BOARDING_IMAGE_ID)
        }
        AnimatedContent(
            modifier = animatedContentModifier,
            targetState = onBoardingImageResId,
            label = ""
        ) { targetOnBoardingImageResId ->
            ImagePainterStable(
                modifier = Modifier,
                drawableResId = targetOnBoardingImageResId,
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
        }


        Text(
            modifier = Modifier
                .fillMaxWidth()
                .layoutId(LayoutUtil.DESC_ID),
            textAlign = TextAlign.Center,
            text = stringResource(descResId),
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.W500
            ),
            color = Grey45
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .layoutId(LayoutUtil.TABS_ID),
            horizontalArrangement = Arrangement.Center

        ) {
            val firstBulletPointModifier = remember(selectedTabIndex) {
                Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .drawWithCache {
                        onDrawWithContent {
                            drawCircle(color = if (selectedTabIndex == 0) Orange90 else Grey71)
                        }
                    }
            }
            Box(firstBulletPointModifier)
            Spacer(Modifier.width(8.dp))

            val secondBulletPointModifier = remember(selectedTabIndex) {
                Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .drawWithCache {
                        onDrawWithContent {
                            drawCircle(color = if (selectedTabIndex == 1) Orange90 else Grey71)
                        }
                    }
            }
            Box(secondBulletPointModifier)
            Spacer(Modifier.width(8.dp))

            val thirdBulletPointModifier = remember(selectedTabIndex) {
                Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .drawWithCache {
                        onDrawWithContent {
                            drawCircle(color = if (selectedTabIndex == 2) Orange90 else Grey71)
                        }
                    }
            }
            Box(thirdBulletPointModifier)
        }


        val buttonModifier = remember {
            Modifier
                .height(44.dp)
                .widthIn(min = buttonWidthMin)
                .layoutId(LayoutUtil.BUTTON_ID)
        }

        PrimaryButton(
            modifier = buttonModifier,
            contentPadding = PaddingValues(horizontal = 40.dp, vertical = 12.5.dp),
            text = if (selectedTabIndex < 2) {
                stringResource(R.string.selanjutnya)
            } else stringResource(
                R.string.mulai
            ),
            trailingIcon = if (selectedTabIndex < 2) {
                {
                    Icon(
                        Icons.Default.ArrowForward,
                        tint = Color.White,
                        contentDescription = null
                    )
                }
            } else null,
            onClick = {
                if (selectedTabIndex < 2) {
                    selectedTabIndex++
                } else {
                    onUserHasPressedStartButton()
                }
            },
            enabled = isButtonEnabledProvider()
        )
    }
}

val getBirdToLeftCloudMargin = { selectedTabIndex: Int, screenHeightDp: Dp ->
    val leftCloudHeightToParentRatio = 0.1138f
    val cloudHeight = screenHeightDp * leftCloudHeightToParentRatio
    val birdToLeftCloudMarginRatio = when (selectedTabIndex) {
        0 -> 0f
        1 -> -0.75f
        else -> -0.9f
    }
    cloudHeight * birdToLeftCloudMarginRatio
}

val getBirdToParentMargin = { selectedTabIndex: Int, screenWidthDp: Dp ->
    val birdToParentMarginRatio = when (selectedTabIndex) {
        0 -> 0.135f
        1 -> 0.3f
        else -> 0.76f
    }
    (screenWidthDp * birdToParentMarginRatio)
}

val getLeftCloudToParentMargin = { selectedTabIndex: Int, screenWidthDp: Dp ->
    val leftCloudWidthEstimation = (screenWidthDp * 0.55f)
    val ratio = when (selectedTabIndex) {
        0 -> 0f
        1 -> -0.45f
        else -> -0.7f
    }
    (leftCloudWidthEstimation * ratio)
}

val getRightCloudToParentMargin = { selectedTabIndex: Int, screenWidthDp: Dp ->
    val rightCloudWidthEstimation = (screenWidthDp * 0.40f)
    val ratio = when (selectedTabIndex) {
        0, 1 -> -0.2f
        else -> 0f
    }
    (rightCloudWidthEstimation * ratio)
}

@Composable
private fun rememberConstraintSet(): ConstraintSet {
    val configuration = LocalConfiguration.current
    return remember {
        val screenHeightDp =
            configuration.screenHeightDp.dp

        val screenWidthDp =
            configuration.screenWidthDp.dp


        val rightCloudToLeftCloudMarginRatio = -0.0356f
        val rightCloudToLeftCloudMargin = (rightCloudToLeftCloudMarginRatio * screenHeightDp)

        val leftCloudToHeadlineMarginRatio = 0.061f
        val leftCloudToHeadlineMargin = (screenHeightDp * leftCloudToHeadlineMarginRatio)

        ConstraintSet {
            val onBoardingImage = createRefFor(LayoutUtil.ON_BOARDING_IMAGE_ID)
            val headline = createRefFor(LayoutUtil.HEADLINE_ID)
            val desc = createRefFor(LayoutUtil.DESC_ID)
            val tabs = createRefFor(LayoutUtil.TABS_ID)
            val button = createRefFor(LayoutUtil.BUTTON_ID)
            val leftCloud = createRefFor(LayoutUtil.LEFT_CLOUD_ID)
            val rightCloud = createRefFor(LayoutUtil.RIGHT_CLOUD_ID)
            val bird = createRefFor(LayoutUtil.BIRD_ID)

            constrain(leftCloud) {
                bottom.linkTo(headline.top, margin = leftCloudToHeadlineMargin)
                start.linkTo(parent.start, margin = getLeftCloudToParentMargin(0, screenWidthDp))
            }

            constrain(bird) {
                start.linkTo(parent.start, margin = getBirdToParentMargin(0, screenWidthDp))
                bottom.linkTo(leftCloud.top, margin = getBirdToLeftCloudMargin(0, screenHeightDp))
            }

            constrain(rightCloud) {
                end.linkTo(parent.end, margin = getRightCloudToParentMargin(0, screenWidthDp))
                bottom.linkTo(leftCloud.top, margin = rightCloudToLeftCloudMargin)
            }

            constrain(headline) {
                bottom.linkTo(onBoardingImage.top, margin = 40.dp)
            }

            constrain(button) {
                top.linkTo(tabs.bottom, margin = 64.dp)
                start.linkTo(tabs.start)
                end.linkTo(tabs.end)
            }

            constrain(tabs) {
                top.linkTo(desc.bottom, margin = 24.dp)
            }

            constrain(desc) {
                top.linkTo(onBoardingImage.bottom, margin = 32.dp)
            }

            constrain(onBoardingImage) {
                centerVerticallyTo(parent)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun OnBoardingScreenPreview2() {
    KakaoXpertTheme {
        OnBoardingContent()
    }
}