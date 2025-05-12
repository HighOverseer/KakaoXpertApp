package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.toplevel.account

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.neotelemetrixgdscunand.kakaoxpert.R
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Black10
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Grey47
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Grey60
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Grey61
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Grey90
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.KakaoXpertTheme
import com.neotelemetrixgdscunand.kakaoxpert.presentation.utils.AsyncImagePainterStable
import com.neotelemetrixgdscunand.kakaoxpert.presentation.utils.collectChannelWhenStarted

@Composable
fun AccountScreen(
    modifier: Modifier = Modifier,
    navigateToProfile: () -> Unit = {},
    navigateToAuth: (String) -> Unit = {},
    viewModel: AccountViewModel = hiltViewModel()
) {

    val lifecycle = LocalLifecycleOwner.current
    val logoutMessage = stringResource(R.string.kamu_telah_logout)
    LaunchedEffect(true) {
        lifecycle.collectChannelWhenStarted(viewModel.onLogoutFinishedEvent) {
            navigateToAuth(logoutMessage)
        }
    }

    AccountContent(
        modifier = modifier,
        navigateToProfile = navigateToProfile,
        onLogout = viewModel::logout
    )
}

@Composable
fun AccountContent(
    modifier: Modifier = Modifier,
    navigateToProfile: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = Grey90)
            .padding(horizontal = 16.dp),
    ) {
        Spacer(Modifier.height(36.dp))

        Text(
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
            text = stringResource(R.string.akun_dan_pengaturan),
            style = MaterialTheme.typography.headlineSmall,
            color = Black10
        )

        Spacer(Modifier.height(32.dp))

        Card(
            modifier = Modifier
                .clickable(onClick = navigateToProfile)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.White, shape = RoundedCornerShape(8.dp))
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                val circleImageModifier = remember {
                    Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                }

                Box(
                    modifier = circleImageModifier
                ) {
                    AsyncImagePainterStable(
                        modifier = Modifier
                            .align(Alignment.Center),
                        alignment = Alignment.Center,
                        imageDrawableResId = R.drawable.dummy_profile,
                        placeholderResId = R.drawable.ic_camera,
                        contentDescription = stringResource(R.string.profile_photo),
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(Modifier.width(16.dp))

                Column {
                    Text(
                        stringResource(R.string.selamat_datang),
                        style = MaterialTheme.typography.labelMedium,
                        color = Grey60
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        stringResource(R.string.fajar_alif_riyandi),
                        style = MaterialTheme.typography.headlineSmall,
                        color = Black10
                    )
                }

                Spacer(Modifier.weight(1f))

                Icon(
                    imageVector = ImageVector
                        .vectorResource(R.drawable.ic_pencil),
                    tint = Grey61,
                    contentDescription = stringResource(R.string.edit_profile)
                )
            }
        }

        Spacer(Modifier.height(40.dp))

        Text(
            text = stringResource(R.string.bantuan),
            style = MaterialTheme.typography.headlineSmall,
            color = Black10
        )

        Spacer(Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .background(color = Color.White, shape = RoundedCornerShape(8.dp))
                .padding(vertical = 24.dp, horizontal = 16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier
                        .size(20.dp),
                    imageVector = ImageVector
                        .vectorResource(R.drawable.ic_info),
                    contentDescription = null
                )

                Spacer(Modifier.width(16.dp))

                Text(
                    stringResource(R.string.tentang_aplikasi),
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Normal
                    )
                )

                Spacer(Modifier.weight(1f))

                Icon(
                    modifier = Modifier
                        .width(7.7.dp)
                        .height(11.dp),
                    imageVector = ImageVector
                        .vectorResource(R.drawable.ic_right_arrow),
                    contentDescription = null,
                    tint = Grey47
                )
            }

            Spacer(Modifier.height(34.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier
                        .size(20.dp),
                    imageVector = ImageVector
                        .vectorResource(R.drawable.ic_comment),
                    contentDescription = null
                )

                Spacer(Modifier.width(16.dp))

                Text(
                    stringResource(R.string.berikan_saran_kepada_kami),
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Normal
                    )
                )

                Spacer(Modifier.weight(1f))

                Icon(
                    modifier = Modifier
                        .width(7.7.dp)
                        .height(11.dp),
                    imageVector = ImageVector
                        .vectorResource(R.drawable.ic_right_arrow),
                    contentDescription = null,
                    tint = Grey47
                )
            }

            Spacer(Modifier.height(34.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier
                        .size(20.dp),
                    imageVector = ImageVector
                        .vectorResource(R.drawable.ic_phone),
                    contentDescription = null
                )

                Spacer(Modifier.width(16.dp))

                Text(
                    stringResource(R.string.hubungi_kami),
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Normal
                    )
                )

                Spacer(Modifier.weight(1f))

                Icon(
                    modifier = Modifier
                        .width(7.7.dp)
                        .height(11.dp),
                    imageVector = ImageVector
                        .vectorResource(R.drawable.ic_right_arrow),
                    contentDescription = null,
                    tint = Grey47
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.akun),
            style = MaterialTheme.typography.headlineSmall,
            color = Black10
        )

        Spacer(Modifier.height(16.dp))

        Card(
            Modifier
                .clickable(onClick = onLogout)
        ) {
            Column(
                modifier = Modifier
                    .background(color = Color.White, shape = RoundedCornerShape(8.dp))
                    .padding(vertical = 24.dp, horizontal = 16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        modifier = Modifier
                            .size(20.dp),
                        imageVector = ImageVector
                            .vectorResource(R.drawable.ic_logout),
                        contentDescription = null
                    )

                    Spacer(Modifier.width(16.dp))

                    Text(
                        stringResource(R.string.keluar),
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Normal
                        )
                    )

                    Spacer(Modifier.weight(1f))

                    Icon(
                        modifier = Modifier
                            .width(7.7.dp)
                            .height(11.dp),
                        imageVector = ImageVector
                            .vectorResource(R.drawable.ic_right_arrow),
                        contentDescription = null,
                        tint = Grey47
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun AccountScreenPreview() {
    KakaoXpertTheme {
        AccountScreen()
    }

}