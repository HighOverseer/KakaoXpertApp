package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.neotelemetrixgdscunand.kakaoxpert.R
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Black10
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.KakaoXpertTheme
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Green55
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.auth.component.AuthTextField
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.auth.component.PrimaryButton
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.util.AsyncImagePainterStable

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    navigateUp: () -> Unit = {},
) {
    ProfileContent(
        modifier = modifier,
        navigateUp = navigateUp
    )
}

@Composable
fun ProfileContent(
    modifier: Modifier = Modifier,
    navigateUp: () -> Unit = { },
) {
    val scrollState = rememberScrollState()
    var name by rememberSaveable { mutableStateOf("Fajar Alif Riyandi") }
    val nameInteractionSource = remember { MutableInteractionSource() }
    val nameTextFieldFocused by nameInteractionSource.collectIsFocusedAsState()
    var hpNumber by rememberSaveable { mutableStateOf("081234567890") }
    val hpNumberInteractionSource = remember { MutableInteractionSource() }
    val hpNumberTextFieldFocused by hpNumberInteractionSource.collectIsFocusedAsState()
    val textMaxLength = 25
    val hpNumberMaxLength = 13

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(scrollState)
    ) {
        Spacer(Modifier.height(32.dp))

        Box(
            Modifier
                .fillMaxWidth()
        ) {
            IconButton(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .align(Alignment.CenterStart),
                onClick = navigateUp
            ) {
                Icon(
                    modifier = Modifier
                        .size(20.dp),
                    imageVector = ImageVector
                        .vectorResource(R.drawable.ic_arrow_left),
                    tint = Black10,
                    contentDescription = stringResource(R.string.kembali)
                )
            }
            Text(
                modifier = Modifier
                    .align(Alignment.Center),
                text = stringResource(R.string.ubah_profil),
                style = MaterialTheme.typography.headlineSmall,
                color = Black10
            )
        }

        Spacer(
            Modifier
                .heightIn(min = 16.dp, max = 64.dp)
        )


        Box(
            Modifier
                .align(Alignment.CenterHorizontally)
        ) {
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .clip(CircleShape)
                    .background(Green55)
                    .fillMaxWidth(0.31f)
                    .aspectRatio(1f),
                contentAlignment = Alignment.Center
            ) {
                AsyncImagePainterStable(
                    placeholderResId = R.drawable.ic_camera,
                    alignment = Alignment.Center,
                    imageDrawableResId = R.drawable.dummy_profile,
                    contentScale = ContentScale.Crop,
                )
            }


            IconButton(
                onClick = {},
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .align(Alignment.BottomEnd)
                    .background(color = Color.White, shape = CircleShape)
                    .size(40.dp),
            ) {
                Icon(
                    modifier = Modifier
                        .size(18.dp)
                        .padding(0.dp),
                    imageVector = ImageVector.vectorResource(R.drawable.ic_pencil),
                    contentDescription = null,
                    tint = Green55
                )
            }
        }

        Column(
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Spacer(
                Modifier
                    .height(24.dp)
                    .heightIn(min = 16.dp, max = 64.dp)
            )

            AuthTextField(
                title = stringResource(R.string.nama),
                onValueChange = {
                    if (it.length <= textMaxLength) {
                        name = it
                    }
                },
                hintText = stringResource(R.string.masukan_nama_kamu_disini),
                valueProvider = { name },
                isFocusedProvider = { nameTextFieldFocused }
            )

            Spacer(Modifier.height(16.dp))

            AuthTextField(
                title = stringResource(R.string.nomor_handphone),
                onValueChange = {
                    if (it.length <= hpNumberMaxLength) {
                        hpNumber = it
                    }
                },
                hintText = stringResource(R.string.masukan_nama_kamu_disini),
                valueProvider = { hpNumber },
                isFocusedProvider = { hpNumberTextFieldFocused }
            )

            Spacer(
                Modifier
                    .height(64.dp)
                    .heightIn(min = 32.dp, max = 84.dp)
            )

            PrimaryButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.simpan),
                onClick = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileScreenPreview() {
    KakaoXpertTheme {
        ProfileScreen()
    }
}