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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.neotelemetrixgdscunand.kakaoxpert.R
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.KakaoXpertTheme
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.auth.component.AuthHeaderSection
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.auth.component.AuthTextField
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.auth.component.PrimaryButton
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.auth.component.SmallLogoHeadline
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.takephoto.component.SecondaryButton
import com.neotelemetrixgdscunand.kakaoxpert.presentation.utils.collectChannelWhenStarted


@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    viewModel: RegisterViewModel = hiltViewModel(),
    navigateToOnBoarding: () -> Unit = {},
    showSnackbar: (String) -> Unit = { },
    navigateBackToLogin: () -> Unit = {}
) {

    val lifecycle = LocalLifecycleOwner.current
    val context = LocalContext.current
    LaunchedEffect(true) {
        lifecycle.collectChannelWhenStarted(viewModel.uiEvent) { event ->
            when (event) {
                is RegisterUIEvent.OnRegisterFailed -> {
                    val message = event.messageUIText.getValue(context)
                    showSnackbar(message)
                }

                is RegisterUIEvent.OnRegisterSuccess -> {
                    showSnackbar(
                        context.getString(
                            R.string.daftar_berhasil_selamat_datang,
                            event.userName
                        )
                    )
                    navigateToOnBoarding()
                }
            }
        }
    }

    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()

    RegisterContent(
        modifier = modifier,
        canInteract = !isLoading,
        onRegister = viewModel::register,
        navigateBackToLogin = navigateBackToLogin
    )
}


@Composable
fun RegisterContent(
    modifier: Modifier = Modifier,
    canInteract: Boolean = true,
    onRegister: (String, String, String, String) -> Unit = { _, _, _, _ -> },
    navigateBackToLogin: () -> Unit = { }
) {
    var name by rememberSaveable { mutableStateOf("") }
    var username by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var confirmationPassword by rememberSaveable { mutableStateOf("") }

    val nameInteractionSource = remember { MutableInteractionSource() }
    val usernameInteractionSource = remember { MutableInteractionSource() }
    val passwordInteractionSource = remember { MutableInteractionSource() }
    val confirmationPasswordInteractionSource = remember { MutableInteractionSource() }

    var isPasswordVisible by rememberSaveable { mutableStateOf(false) }
    var isConfirmationPassswordVisible by rememberSaveable { mutableStateOf(false) }
    val textMaxLength = 50

    val topMarginToContentRatio = 0.0838f
    val headlineToLogoMarginRatio = 0.0505f
    val formToHeadlineMarginRatio = 0.0397f
    val buttonToFormMarginRatio = 0.0452f

    val configuration = LocalConfiguration.current
    val topMarginToContentDp = (configuration.screenHeightDp * topMarginToContentRatio).dp
    val headlineToLogoMarginDp = (configuration.screenHeightDp * headlineToLogoMarginRatio).dp
    val formToHeadlineMarginDp = (configuration.screenHeightDp * formToHeadlineMarginRatio).dp
    val buttonToFormMarginDp = (configuration.screenHeightDp * buttonToFormMarginRatio).dp


    val isNameTextFieldFocused by nameInteractionSource.collectIsFocusedAsState()
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
                .height(topMarginToContentDp)
        )

        SmallLogoHeadline()

        Spacer(
            modifier = Modifier
                .height(headlineToLogoMarginDp)
        )

        AuthHeaderSection(
            modifier = Modifier.padding(horizontal = 16.dp),
            title = stringResource(R.string.buat_akun),
            subTitle = stringResource(R.string.daftar_untuk_akses_fitur_deteksi_penyakit)
        )

        Spacer(
            modifier = Modifier
                .height(formToHeadlineMarginDp)
        )

        Column(
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {

            AuthTextField(
                title = stringResource(R.string.nama),
                interactionSource = nameInteractionSource,
                onValueChange = {
                    if (it.length <= textMaxLength) {
                        name = it
                    }
                },
                hintText = stringResource(R.string.masukan_nama_kamu_disini),
                valueProvider = { name },
                isFocusedProvider = { isNameTextFieldFocused },
                enabled = canInteract
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

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
                isFocusedProvider = { isUsernameTextFieldFocused },
                enabled = canInteract
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            AuthTextField(
                title = stringResource(R.string.password),
                interactionSource = passwordInteractionSource,
                onValueChange = {
                    if (it.length <= textMaxLength) {
                        password = it
                    }
                },
                valueProvider = { password },
                isFocusedProvider = { isPasswordTextFieldFocused },
                hintText = stringResource(R.string.masukkan_password_kamu_disini),
                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    Icon(
                        modifier = Modifier
                            .clickable {
                                isPasswordVisible = !isPasswordVisible
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
                },
                enabled = canInteract
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            AuthTextField(
                title = stringResource(R.string.konfirmasi_password),
                interactionSource = confirmationPasswordInteractionSource,
                onValueChange = {
                    if (it.length <= textMaxLength) {
                        confirmationPassword = it
                    }
                },
                valueProvider = { confirmationPassword },
                hintText = stringResource(R.string.masukan_ulang_password),
                isFocusedProvider = { isConfirmationPasswordTextFieldFocused },
                visualTransformation = if (isConfirmationPassswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    Icon(
                        modifier = Modifier
                            .clickable {
                                isConfirmationPassswordVisible = !isConfirmationPassswordVisible
                            },
                        imageVector = if (isConfirmationPassswordVisible)
                            ImageVector
                                .vectorResource(
                                    R.drawable.ic_eye,
                                )
                        else ImageVector
                            .vectorResource(R.drawable.ic_eye),
                        contentDescription = stringResource(R.string.visibilitas_password)
                    )
                },
                enabled = canInteract
            )


            Spacer(
                modifier = Modifier.height(buttonToFormMarginDp)
            )

            PrimaryButton(
                modifier = Modifier
                    .fillMaxWidth(),
                text = stringResource(R.string.daftar),
                onClick = {
                    onRegister(
                        name,
                        username,
                        password,
                        confirmationPassword
                    )
                },
                enabled = canInteract
            )

            Spacer(Modifier.height(16.dp))

            SecondaryButton(
                modifier = Modifier
                    .fillMaxWidth(),
                text = stringResource(R.string.sudah_punya_akun_login_yuk),
                onClick = navigateBackToLogin,
                enabled = canInteract
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