package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.notif.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.unit.sp
import com.neotelemetrixgdscunand.kakaoxpert.R
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Black10
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Black30
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Green55
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Grey60
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Grey71
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Grey80
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Grey90
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.KakaoXpertTheme
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Orange90
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.notif.component.CacaoProfileContent
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.notif.component.CacaoTechnicalSpec
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.notif.component.InputFulfillAmountCacaoDialog
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.notif.component.SupplierItem
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.util.AsyncImagePainterStable
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.util.ImagePainterStable
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.getSupplierCacaoDummies
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.auth.component.PrimaryButton

@Composable
fun CacaoRequestScreen(
    modifier: Modifier = Modifier,
    navigateUp: () -> Unit = {},
    currentAmountFulfilled: Float = 0f,
    totalAmountDemands: Float = 0f
) {

    val progressFraction = remember(currentAmountFulfilled, totalAmountDemands) {
        val progress = currentAmountFulfilled / totalAmountDemands
        progress.coerceIn(0f, 1f)
    }

    val lazyListColumnState = rememberLazyListState()

    val outermostPaddingModifier = remember {
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    }

    val lazyListRowState = rememberLazyListState()
    val cacaoPhotos = remember {
        List(6) {
            "https://s3-alpha-sig.figma.com/img/86ec/eb1d/880b37b9a3edc5d734e482b54e2415bc?Expires=1737936000&Key-Pair-Id=APKAQ4GOSFWCVNEHN3O4&Signature=QQUE6G7FyaWre22eVVZ95skNbVWTAoWFb-GsakTBJprz5iPrARUDjgW7l92p4cuvBWv3UiDFGEVKkJZrjV4z5kvHTX7pE3gDUCSSAFCfmiZyxKKFhiYS15fY4DQh98Rpoc8bLU6qVkmFQeELF103D2JHQwgS~hPUXO36r3kOwdXZXFKn~OSRd-WNMryy9KKzCrckA-mrNoJlkjKI5Y7upLj0uv1h-Ouo9501Wkv0i4rzeeQhhLUqHZ6D5CuxvMX3mGdAOCGgXLXl5Ypk~6z5a7WxE2~8Hh0jIfx36nqmftcGm6bRA4e8AbjCBqHVtux7-v4IU4onadWX-UnBPpuJoA__"
        }
    }

    val allowedCharacterPattern = remember {
        "^[0-9]*[,]{0,1}[0-9]*$".toRegex()
    }

    var isDialogShowed by remember { mutableStateOf(false) }

    var inputFulfillAmount by remember { mutableStateOf("") }

    val suppliers = remember {
        getSupplierCacaoDummies()
    }

    val supplierItemModifier = remember {
        outermostPaddingModifier
            .background(color = Color.White)
            .padding(horizontal = 16.dp)
    }


    LazyColumn(
        state = lazyListColumnState,
        modifier = modifier
            .background(color = Grey90)
    ) {

        item {
            Spacer(Modifier.height(20.dp))
        }

        item {
            Box(
                Modifier.fillMaxWidth()
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
                    text = stringResource(R.string.notifikasi),
                    style = MaterialTheme.typography.headlineSmall,
                    color = Black10
                )
            }
        }


        item {
            Spacer(Modifier.height(16.dp))

            Column(
                outermostPaddingModifier
                    .background(color = Color.White, shape = RoundedCornerShape(8.dp))
                    .padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ImagePainterStable(
                        drawableResId = R.drawable.ic_cube,
                        contentDescription = null
                    )

                    Spacer(Modifier.width(10.dp))

                    Text(
                        text = stringResource(R.string.jumlah_terpenuhi),
                        style = MaterialTheme.typography.labelMedium.copy(
                            lineHeight = 20.sp,
                            letterSpacing = (-0.02).sp
                        ),
                        modifier = Modifier.weight(1f),
                        color = Black10
                    )

                    Text(
                        text = stringResource(
                            R.string.ton,
                            currentAmountFulfilled,
                            totalAmountDemands
                        ),
                        style = MaterialTheme.typography.labelMedium.copy(
                            lineHeight = 20.sp,
                            letterSpacing = (-0.02).sp
                        ),
                        color = Green55
                    )
                }

                Spacer(Modifier.height(16.dp))

                Box(
                    contentAlignment = Alignment.CenterStart,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .background(color = Grey71, shape = RoundedCornerShape(2.dp)),
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(progressFraction)
                            .height(6.dp)
                            .background(color = Green55, shape = RoundedCornerShape(2.dp)),
                    )
                }
            }
        }

        item {
            Spacer(Modifier.height(24.dp))

            Column(
                outermostPaddingModifier
                    .background(color = Color.White, shape = RoundedCornerShape(8.dp))
                    .padding(vertical = 24.dp, horizontal = 16.dp),

                ) {
                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top
                ) {
                    ImagePainterStable(
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(44.dp),
                        drawableResId = R.drawable.ic_camera,
                        contentScale = ContentScale.Crop,
                        contentDescription = null
                    )

                    Spacer(Modifier.width(16.dp))
                    Column {
                        Row(
                            Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Anthony Gasing",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    lineHeight = 15.sp,
                                    letterSpacing = (-0.02).sp
                                ),
                                color = Orange90,
                                modifier = Modifier.weight(1f)
                            )

                            Text(
                                "Jan 24",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    lineHeight = 20.sp,
                                    letterSpacing = (-0.02).sp
                                ),
                                color = Grey60
                            )

                        }
                        Spacer(Modifier.height(8.dp))

                        Text(
                            "Membutuhkan 10 Ton Kakao dengan varietas Criollo tawaran harga sebesar \$25.000",
                            style = MaterialTheme.typography.labelMedium.copy(
                                lineHeight = 20.sp,
                                letterSpacing = (-0.02).sp
                            ),
                            color = Black30
                        )
                    }
                }
            }
        }

        item {
            Spacer(Modifier.height(24.dp))

            Text(
                stringResource(R.string.foto_kakao),
                style = MaterialTheme.typography.titleLarge.copy(
                    lineHeight = 15.sp,
                    letterSpacing = (-0.02).sp
                ),
                color = Black30,
                modifier = outermostPaddingModifier
            )

            Spacer(Modifier.height(16.dp))
        }


        item {
            LazyRow(
                state = lazyListRowState,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                items(cacaoPhotos) {
                    AsyncImagePainterStable(
                        imageUrlOrPath = it,
                        alignment = Alignment.Center,
                        placeholderResId = R.drawable.ic_camera,
                        contentScale = ContentScale.Crop,
                        contentDescription = null,
                        modifier = Modifier
                            .width(114.dp)
                            .height(113.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )
                }
            }
        }


        item {
            Spacer(Modifier.height(24.dp))

            Column(
                outermostPaddingModifier
                    .background(color = Color.White, shape = RoundedCornerShape(12.dp))
                    .padding(vertical = 24.dp, horizontal = 16.dp)
            ) {
                Text(
                    stringResource(R.string.cocoa_profile),
                    style = MaterialTheme.typography.titleLarge.copy(
                        lineHeight = 15.sp,
                        letterSpacing = (-0.02).sp
                    ),
                    color = Black30
                )

                Spacer(Modifier.height(32.dp))

                Row {
                    CacaoProfileContent(
                        modifier = Modifier.weight(0.45f),
                        iconResId = R.drawable.ic_leaf,
                        titleResId = R.string.varietas,
                        text = "Criollo"
                    )

                    CacaoProfileContent(
                        modifier = Modifier.weight(0.55f),
                        iconResId = R.drawable.ic_color,
                        titleResId = R.string.warna,
                        text = "Cokelat keemasan, permukaan halus"
                    )
                }

                Spacer(Modifier.height(24.dp))

                Row {
                    CacaoProfileContent(
                        modifier = Modifier.weight(0.45f),
                        iconResId = R.drawable.ic_nut,
                        titleResId = R.string.kacang,
                        text = "100 kacang = 1,2 kg"
                    )

                    CacaoProfileContent(
                        modifier = Modifier.weight(0.55f),
                        iconResId = R.drawable.ic_clock_maroon,
                        titleResId = R.string.tingkat_fermentasi,
                        text = "85%"
                    )
                }

                Spacer(Modifier.height(32.dp))

                Text(
                    stringResource(R.string.spesifikasi_teknis),
                    style = MaterialTheme.typography.titleLarge.copy(
                        lineHeight = 15.sp,
                        letterSpacing = (-0.02).sp
                    ),
                    color = Black30
                )

                Spacer(Modifier.height(32.dp))

                Row {
                    CacaoTechnicalSpec(
                        modifier = Modifier.weight(0.45f),
                        titleResId = R.string.kandungan_lemak,
                        text = "54%"
                    )

                    CacaoTechnicalSpec(
                        modifier = Modifier.weight(0.55f),
                        titleResId = R.string.kandungan_kelembaban,
                        text = "7%"
                    )
                }

                Spacer(Modifier.height(38.dp))

                Row {
                    CacaoTechnicalSpec(
                        modifier = Modifier.weight(0.45f),
                        titleResId = R.string.keasaman_ph,
                        text = "5.5"
                    )

                    CacaoTechnicalSpec(
                        modifier = Modifier.weight(0.55f),
                        titleResId = R.string.kualitas,
                        text = "A (standar internasional ICCO)"
                    )
                }
            }
        }

        item {
            Spacer(Modifier.height(24.dp))

            Column(
                outermostPaddingModifier
                    .background(color = Color.White, shape = RoundedCornerShape(8.dp))
                    .padding(vertical = 24.dp, horizontal = 16.dp)
            ) {
                Text(
                    stringResource(R.string.daftar_supplier_saat_ini),
                    style = MaterialTheme.typography.titleLarge.copy(
                        lineHeight = 15.sp,
                        letterSpacing = (-0.02).sp
                    ),
                    color = Black30
                )

            }
        }

        itemsIndexed(suppliers, key = { _, it -> it.id }) { i, it ->
            SupplierItem(
                supplierName = it.name,
                supplierImageUrl = it.imageUrl,
                modifier = supplierItemModifier
            )

            if (i < suppliers.lastIndex) {
                Spacer(
                    outermostPaddingModifier
                        .height(16.dp)
                        .background(color = Color.White)
                )
                Box(
                    outermostPaddingModifier
                        .height(1.dp)
                        .padding(horizontal = 16.dp)
                        .background(color = Grey80)
                )
                Spacer(
                    outermostPaddingModifier
                        .height(16.dp)
                        .background(color = Color.White)
                )
            }
        }

        item {
            Spacer(
                outermostPaddingModifier
                    .height(64.dp)
                    .background(color = Color.White)
            )
        }

        item {
            Column(
                outermostPaddingModifier
                    .background(
                        Color.White,
                        shape = RoundedCornerShape(bottomEnd = 8.dp, bottomStart = 8.dp)
                    )
                    .padding(horizontal = 16.dp)
            ) {

                PrimaryButton(
                    text = stringResource(R.string.sanggupi),
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        isDialogShowed = true
                    }
                )

                Spacer(Modifier.height(24.dp))
            }
        }

    }

    InputFulfillAmountCacaoDialog(
        onValueChange = {
            if (allowedCharacterPattern.matches(it)) {
                inputFulfillAmount = it
            }
        },
        isShowDialog = isDialogShowed,
        onDismiss = {
            isDialogShowed = false
        },
        value = inputFulfillAmount,
        onSubmit = {
            // TODO()
            isDialogShowed = false
        }
    )
}

@Preview(showBackground = true, heightDp = 1500)
@Composable
private fun CacaoRequestScreenPreview() {
    KakaoXpertTheme {
        CacaoRequestScreen()
    }
}