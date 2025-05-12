package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.notif.component

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.neotelemetrixgdscunand.kakaoxpert.R
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Black10
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Grey90
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.KakaoXpertTheme
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.auth.component.PrimaryButton
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.auth.component.PrimaryTextField
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.takephoto.component.SecondaryButton

@Composable
fun InputFulfillAmountCacaoDialog(
    isShowDialog: Boolean = false,
    onDismiss: () -> Unit = { },
    value: String = "",
    onValueChange: (String) -> Unit = {},
    onSubmit: () -> Unit = {}
) {

    if (isShowDialog) {
        val interactionSource = remember {
            MutableInteractionSource()
        }

        val isFocused by interactionSource.collectIsFocusedAsState()

        Dialog(
            onDismissRequest = onDismiss
        ) {
            Card(
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 4.dp
                ),
            ) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 32.dp)
                ) {

                    Text(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        text = stringResource(R.string.berapa_jumlah_yang_dapat_kamu_penuhi),
                        style = MaterialTheme.typography.titleMedium,
                        color = Black10
                    )

                    Spacer(Modifier.height(24.dp))

                    PrimaryTextField(
                        contentPadding = PaddingValues(vertical = 13.5.dp, horizontal = 16.dp),
                        hintText = stringResource(R.string.masukkan_jumlah),
                        valueProvider = { value },
                        onValueChange = onValueChange,
                        textColor = Black10,
                        backgroundColor = Grey90,
                        isFocusedProvider = { isFocused },
                        interactionSource = interactionSource,
                        isBordered = false
                    )

                    Spacer(Modifier.height(24.dp))

                    Row {
                        SecondaryButton(
                            modifier = Modifier.weight(1f),
                            text = stringResource(R.string.batal),
                            contentPadding = PaddingValues(vertical = 14.dp),
                            onClick = onDismiss
                        )

                        Spacer(Modifier.width(14.dp))


                        PrimaryButton(
                            modifier = Modifier.weight(1f),
                            text = stringResource(R.string.selesai),
                            contentPadding = PaddingValues(vertical = 14.dp),
                            onClick = onSubmit
                        )

                    }


                }
            }
        }
    }

}

@Preview
@Composable
private fun InputFulfillAmountCacaoPreview() {
    KakaoXpertTheme {
        InputFulfillAmountCacaoDialog(
            isShowDialog = true
        )
    }
}