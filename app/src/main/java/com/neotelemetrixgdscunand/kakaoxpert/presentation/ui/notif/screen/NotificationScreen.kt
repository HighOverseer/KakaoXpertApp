package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.notif.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.neotelemetrixgdscunand.kakaoxpert.R
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Black10
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Grey90
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.KakaoXpertTheme
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.notif.component.NotificationItem
import com.neotelemetrixgdscunand.kamekapp.domain.model.getNotificationItemDummies

@Composable
fun NotificationScreen(
    modifier: Modifier = Modifier,
    navigateUp: () -> Unit = { },
    navigateToCacaoRequestScreen: () -> Unit = { }
) {

    val outermostPaddingModifier = remember {
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    }

    val lazyListColumnState = rememberLazyListState()

    val notificationItems = remember {
        getNotificationItemDummies()
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
        }

        items(notificationItems, key = { it.id }) {
            NotificationItem(
                currentAmountFulfilled = it.currentAmountFulfilled,
                totalAmountDemands = it.totalAmountDemands,
                isAgreed = it.isAgreed,
                exporterName = it.exporterName,
                exporterImageUrl = it.exporterImageUrl,
                sendDate = it.sendDate,
                message = it.message,
                modifier = outermostPaddingModifier,
                navigateToDetail = navigateToCacaoRequestScreen
            )

            Spacer(Modifier.height(16.dp))
        }
    }
}

@Preview
@Composable
private fun NotificationScreenPreview() {
    KakaoXpertTheme {
        NotificationScreen()
    }
}