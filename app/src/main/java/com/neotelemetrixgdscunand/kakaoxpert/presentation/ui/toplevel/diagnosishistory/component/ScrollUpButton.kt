package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.toplevel.diagnosishistory.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Green55Alpha70

@Composable
fun ScrollUpButton(
    modifier: Modifier = Modifier, onClick: () -> Unit = { },
    lazyListStateProvider: () -> LazyListState,
    indexItemThreshold: Int = 0
) {
    val isScrollUpButtonVisible by remember {
        derivedStateOf {
            lazyListStateProvider().firstVisibleItemIndex > indexItemThreshold
        }
    }

    AnimatedVisibility(
        visible = isScrollUpButtonVisible,
        modifier = modifier
    ) {

        IconButton(
            onClick = onClick,
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .background(Green55Alpha70, shape = CircleShape)
                    .padding(8.dp)
            ) {
                Icon(
                    modifier = Modifier.fillMaxSize(),
                    imageVector = Icons.Default.KeyboardArrowUp,
                    contentDescription = null,
                    tint = Color.White,
                )
            }
        }
    }
}