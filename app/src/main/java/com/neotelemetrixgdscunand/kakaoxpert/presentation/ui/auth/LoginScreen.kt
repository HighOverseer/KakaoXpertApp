package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.auth


import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.neotelemetrixgdscunand.kakaoxpert.R
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Grey60
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.KakaoXpertTheme
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.auth.component.AuthHeaderSection
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.auth.component.AuthTextField
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.auth.component.PrimaryButton
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.auth.component.SmallLogoHeadline


@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    navigateToOnBoarding: () -> Unit = {}
) {


    LoginContent(
        modifier = modifier,
        navigateToOnBoarding = navigateToOnBoarding
    )
}


@Composable
fun LoginContent(
    modifier: Modifier = Modifier,
    navigateToOnBoarding: () -> Unit = {}
) {

    val scrollState = rememberScrollState()
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
    ) {

        val configuration = LocalConfiguration.current
        val topMarginToContentDp = (configuration.screenHeightDp * 0.0838f).dp
        val headlineToLogoMarginDp = (configuration.screenHeightDp * 0.0805f).dp
        val formToHeadlineMarginDp = (configuration.screenHeightDp * 0.0697f).dp
        val buttonToFormMarginDp = (configuration.screenHeightDp * 0.0872f).dp

        Spacer(
            modifier = Modifier
                .height(topMarginToContentDp)
        )

        SmallLogoHeadline()

        Spacer(
            modifier = Modifier
                .height(headlineToLogoMarginDp)
        )

        AuthHeaderSection(
            modifier = Modifier.padding(horizontal = 16.dp),
            title = stringResource(R.string.masuk),
            subTitle = stringResource(R.string.masuk_untuk_akses_fitur_deteksi_penyakit)
        )

        Spacer(
            modifier = Modifier
                .height(formToHeadlineMarginDp)
        )

        Column(
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {

            val usernameInteractionSource = remember { MutableInteractionSource() }
            val passwordInteractionSource = remember { MutableInteractionSource() }
            val isUsernameTextFieldFocused by usernameInteractionSource.collectIsFocusedAsState()
            val isPasswordTextFieldFocused by passwordInteractionSource.collectIsFocusedAsState()

            var username by remember { mutableStateOf("") }
            var password by remember { mutableStateOf("") }
            var isPasswordVisible by remember { mutableStateOf(false) }
            val textMaxLength = 30

            AuthTextField(
                title = stringResource(R.string.email),
                interactionSource = usernameInteractionSource,
                onValueChange = {
                    if (it.length <= textMaxLength) {
                        username = it
                    }
                },
                hintText = stringResource(R.string.masukan_email_kamu_disini),
                valueProvider = { username },
                isFocusedProvider = { isUsernameTextFieldFocused }
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            AuthTextField(
                title = stringResource(R.string.password),
                interactionSource = passwordInteractionSource,
                onValueChange = {
                    if (it.length <= 30) {
                        password = it
                    }
                },
                valueProvider = { password },
                hintText = stringResource(R.string.masukkan_password_kamu_disini),
                isFocusedProvider = { isPasswordTextFieldFocused },
                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    Icon(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable {
                                isPasswordVisible = !isPasswordVisible
                            },
                        imageVector = if (isPasswordVisible)
                            ImageVector
                                .vectorResource(
                                    R.drawable.ic_eye,
                                )
                        else ImageVector
                            .vectorResource(R.drawable.ic_hide_eye),
                        tint = Grey60,
                        contentDescription = stringResource(R.string.visibilitas_password)
                    )
                }
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                modifier = Modifier
                    .align(Alignment.End),
                textAlign = TextAlign.Right,
                text = stringResource(R.string.lupa_password),
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = Color.Black
            )

            Spacer(
                modifier = Modifier.height(buttonToFormMarginDp)
            )

            PrimaryButton(
                modifier = Modifier
                    .fillMaxWidth(),
                text = stringResource(R.string.masuk),
                onClick = navigateToOnBoarding
            )

            Spacer(Modifier.height(32.dp))

        }


    }
}


@Preview(showBackground = true)
@Composable
private fun LoginContentPreview() {
    KakaoXpertTheme {
        LoginContent()
    }
}