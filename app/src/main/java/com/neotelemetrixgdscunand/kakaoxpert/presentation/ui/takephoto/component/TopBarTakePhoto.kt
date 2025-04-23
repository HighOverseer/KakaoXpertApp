package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.takephoto.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.neotelemetrixgdscunand.kakaoxpert.R
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.KakaoXpertTheme

@Composable
fun TopBarTakePhoto(
    modifier: Modifier = Modifier,
    switchCameraLens: () -> Unit = {},
    cancelSession: () -> Unit = {}
) {
    val topBarModifier = remember {
        modifier
            .background(color = Color.White)
            .padding(start = 16.dp, end = 16.dp, top = 32.dp, bottom = 10.dp)
    }

    Row(
        modifier = topBarModifier,
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Image(
            modifier = Modifier
                .clickable(onClick = cancelSession),
            imageVector = ImageVector.vectorResource(
                id = R.drawable.ic_close
            ),
            contentDescription = stringResource(R.string.cancel_action)
        )

        Image(
            modifier = Modifier
                .clickable(onClick = switchCameraLens),
            imageVector = ImageVector
                .vectorResource(R.drawable.ic_switch_camera),
            contentDescription = stringResource(R.string.switch_camera)
        )
    }
}


@Preview
@Composable
private fun TopBarTakePhotoPreview() {
    KakaoXpertTheme {
        TopBarTakePhoto(
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}