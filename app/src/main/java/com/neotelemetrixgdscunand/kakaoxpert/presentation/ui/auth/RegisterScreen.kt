package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.auth


import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.neotelemetrixgdscunand.kakaoxpert.R
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.KakaoXpertTheme
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.auth.component.AuthHeaderSection
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.auth.component.AuthTextField
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.auth.component.PrimaryButton
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.auth.component.SmallLogoHeadline


@Composable
fun RegisterScreen(modifier: Modifier = Modifier) {

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmationPassword by remember { mutableStateOf("") }
    val usernameInteractionSource = remember { MutableInteractionSource() }
    val passwordInteractionSource = remember { MutableInteractionSource() }
    val confirmationPasswordInteractionSource = remember { MutableInteractionSource() }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isConfirmationPassswordVisible by remember { mutableStateOf(false) }
    val textMaxLength = 30

    RegisterContent(
        modifier = modifier,
        usernameProvider = { username },
        passwordProvider = { password },
        confirmationPasswordProvider = { confirmationPassword },
        onUsernameChange = {
            if (it.length <= textMaxLength) {
                username = it
            }

        },
        onPasswordChange = {
            if (it.length <= textMaxLength) {
                password = it
            }
        },
        onConfirmationPasswordChange = {
            if (it.length <= textMaxLength) {
                confirmationPassword = it
            }
        },
        usernameInteractionSource = usernameInteractionSource,
        passwordInteractionSource = passwordInteractionSource,
        confirmationPasswordInteractionSource = confirmationPasswordInteractionSource,
        isPasswordVisible = isPasswordVisible,
        isConfirmationPasswordVisible = isConfirmationPassswordVisible,
        changePasswordVisibility = {
            isPasswordVisible = it
        },
        changeConfirmationPasswordVisibility = {
            isConfirmationPassswordVisible = it
        }

    )
}


@Composable
fun RegisterContent(
    modifier: Modifier = Modifier,
    usernameProvider: () -> String = { "" },
    passwordProvider: () -> String = { "" },
    confirmationPasswordProvider: () -> String = { "" },
    onUsernameChange: (String) -> Unit = {},
    onPasswordChange: (String) -> Unit = {},
    onConfirmationPasswordChange: (String) -> Unit = {},
    isPasswordVisible: Boolean = false,
    isConfirmationPasswordVisible: Boolean = false,
    changePasswordVisibility: (Boolean) -> Unit = {},
    changeConfirmationPasswordVisibility: (Boolean) -> Unit = {},
    usernameInteractionSource: MutableInteractionSource = MutableInteractionSource(),
    passwordInteractionSource: MutableInteractionSource = MutableInteractionSource(),
    confirmationPasswordInteractionSource: MutableInteractionSource = MutableInteractionSource()
) {
    val topMarginToContentRatio = 0.0838f
    val headlineToLogoMarginRatio = 0.0805f
    val formToHeadlineMarginRatio = 0.0697f
    val buttonToFormMarginRatio = 0.0872f

    val isUsernameTextFieldFocused by usernameInteractionSource.collectIsFocusedAsState()
    val isPasswordTextFieldFocused by passwordInteractionSource.collectIsFocusedAsState()
    val isConfirmationPasswordTextFieldFocused by confirmationPasswordInteractionSource.collectIsFocusedAsState()

    val scrollState = rememberScrollState()
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
    ) {

        Spacer(
            modifier = Modifier
                .fillMaxHeight(topMarginToContentRatio)
        )

        SmallLogoHeadline()

        Spacer(
            modifier = Modifier
                .fillMaxHeight(headlineToLogoMarginRatio)
        )

        AuthHeaderSection(
            modifier = Modifier.padding(horizontal = 16.dp),
            title = stringResource(R.string.buat_akun),
            subTitle = stringResource(R.string.daftar_untuk_akses_fitur_deteksi_penyakit)
        )

        Spacer(
            modifier = Modifier
                .fillMaxHeight(formToHeadlineMarginRatio)
        )

        Column(
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {

            AuthTextField(
                title = stringResource(R.string.email),
                interactionSource = usernameInteractionSource,
                onValueChange = onUsernameChange,
                valueProvider = usernameProvider,
                isFocusedProvider = { isUsernameTextFieldFocused }
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            AuthTextField(
                title = stringResource(R.string.password),
                interactionSource = passwordInteractionSource,
                onValueChange = onPasswordChange,
                valueProvider = passwordProvider,
                isFocusedProvider = { isPasswordTextFieldFocused },
                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    Icon(
                        modifier = Modifier
                            .clickable {
                                changePasswordVisibility(!isPasswordVisible)
                            },
                        imageVector = if (isPasswordVisible)
                            ImageVector
                                .vectorResource(
                                    R.drawable.ic_eye,
                                )
                        else ImageVector
                            .vectorResource(R.drawable.ic_eye),
                        contentDescription = stringResource(R.string.visibilitas_password)
                    )
                }
            )
            Spacer(modifier = Modifier.height(8.dp))

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            AuthTextField(
                title = stringResource(R.string.masukan_ulang_password),
                interactionSource = confirmationPasswordInteractionSource,
                onValueChange = onConfirmationPasswordChange,
                valueProvider = confirmationPasswordProvider,
                isFocusedProvider = { isConfirmationPasswordTextFieldFocused },
                visualTransformation = if (isConfirmationPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    Icon(
                        modifier = Modifier
                            .clickable {
                                changeConfirmationPasswordVisibility(!isConfirmationPasswordVisible)
                            },
                        imageVector = if (isConfirmationPasswordVisible)
                            ImageVector
                                .vectorResource(
                                    R.drawable.ic_eye,
                                )
                        else ImageVector
                            .vectorResource(R.drawable.ic_eye),
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
                modifier = Modifier.fillMaxHeight(buttonToFormMarginRatio)
            )

            PrimaryButton(
                modifier = Modifier
                    .fillMaxWidth(),
                text = stringResource(R.string.daftar)
            )

        }


    }
}


@Preview(showBackground = true)
@Composable
private fun RegisterContentPreview() {
    KakaoXpertTheme {
        RegisterContent()
    }
}