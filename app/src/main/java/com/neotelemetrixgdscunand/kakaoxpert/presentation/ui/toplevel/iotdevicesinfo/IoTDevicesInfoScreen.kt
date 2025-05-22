package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.toplevel.iotdevicesinfo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.neotelemetrixgdscunand.kakaoxpert.R
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.IoTDevice
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Black10
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Grey90
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.KakaoXpertTheme
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Orange85
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.toplevel.IoTDataOverviewMenu
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.toplevel.iotdevicesinfo.component.AddIoTDeviceDialog
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.toplevel.iotdevicesinfo.component.AddIoTDeviceSection
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.toplevel.iotdevicesinfo.component.IoTDeviceDetailDialog
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.toplevel.iotdevicesinfo.component.IoTDeviceItem
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.toplevel.iotdevicesinfo.component.SeeMoreMenu
import com.neotelemetrixgdscunand.kakaoxpert.presentation.utils.collectChannelWhenStarted

@Composable
fun IoTDevicesInfoScreen(
    modifier: Modifier = Modifier,
    navigateToSensorDataDetails: (Int?, String?) -> Unit = {_, _ -> },
    showSnackbar: (String) -> Unit = {},
    viewModel: IoTDeviceInfoViewModel = hiltViewModel()
) {

    val lifecycle = LocalLifecycleOwner.current
    val context = LocalContext.current
    val deviceFormIsNotValidMessage = stringResource(R.string.device_id_or_device_key_is_blank)
    val deviceNotFoundMessage = stringResource(R.string.no_device_found)
    val successAddingDeviceIoTMessage = stringResource(R.string.berhasil_menambahkan_perangkat)
    LaunchedEffect(true) {
        lifecycle.collectChannelWhenStarted(viewModel.event) {
            when (it) {
                is IoTDeviceInfoUIEvent.OnFailedAddingDeviceIoT -> {
                    showSnackbar(it.errorUIText.getValue(context))
                }

                is IoTDeviceInfoUIEvent.OnSuccessAddingDeviceIoT -> {
                    showSnackbar(successAddingDeviceIoTMessage)
                }

                is IoTDeviceInfoUIEvent.OnAddDeviceFormIsNotValid -> {
                    showSnackbar(deviceFormIsNotValidMessage)
                }

                IoTDeviceInfoUIEvent.OnNoDeviceFound -> showSnackbar(deviceNotFoundMessage)
                is IoTDeviceInfoUIEvent.OnFailedGetIoTDataOverview -> {
                    showSnackbar(it.errorUIText.getValue(context))
                }
                is IoTDeviceInfoUIEvent.OnFailedDeletingIoTDeviceIdFromAccount -> {
                    showSnackbar(it.errorUIText.getValue(context))
                }
            }
        }
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    IoTDevicesInfoContent(
        modifier = modifier,
        navigateToSensorDataDetails = navigateToSensorDataDetails,
        uiState = uiState,
        onShowAddDeviceDialog = viewModel::showAddIoTDeviceDialog,
        onSubmitAddDevice = viewModel::addIoTDeviceToAccount,
        onDismissAddDeviceDialog = viewModel::dismissAddIoTDeviceDialog,
        onShowDeviceDetailDialog = viewModel::showDeviceDetailDialog,
        onDismissDeviceDetailDialog = viewModel::dismissDeviceDetailDialog
    )
}


@Composable
fun IoTDevicesInfoContent(
    modifier: Modifier = Modifier,
    navigateToSensorDataDetails: (Int?, String?) -> Unit = {_, _ -> },
    uiState: IoTDeviceInfoUIState = IoTDeviceInfoUIState(),
    onShowAddDeviceDialog: () -> Unit = {},
    onSubmitAddDevice: (String, String) -> Unit = { _, _ -> },
    onDismissAddDeviceDialog: () -> Unit = {},
    onShowDeviceDetailDialog:(IoTDevice) -> Unit = { },
    onDismissDeviceDetailDialog: () -> Unit = { }
) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(color = Grey90)
            .padding(horizontal = 16.dp),
    ) {
        Spacer(Modifier.height(36.dp))

        Text(
            modifier = Modifier
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            text = stringResource(R.string.perangkat),
            style = MaterialTheme.typography.headlineSmall,
            color = Black10
        )

        Spacer(Modifier.height(42.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier
                    .width(16.dp)
                    .widthIn(min = 12.dp, max = 24.dp)
                    .aspectRatio(0.86f),
                imageVector = ImageVector
                    .vectorResource(R.drawable.ic_calendar),
                contentDescription = null,
                tint = Orange85
            )
            Spacer(Modifier.width(10.dp))
            Text(
                stringResource(R.string.overview_data_hari_ini),
                style = MaterialTheme.typography.headlineSmall,
                color = Black10
            )
        }

        Spacer(Modifier.height(16.dp))

        Row {
            IoTDataOverviewMenu(
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1.17f),
                iconResId = R.drawable.ic_thermometer,
                label = stringResource(R.string.suhu),
                value = uiState.ioTDataOverviewDui.averageTemperatureValue.getValue(context),
            )
            Spacer(Modifier.width(16.dp))
            IoTDataOverviewMenu(
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1.17f),
                iconResId = R.drawable.ic_drop,
                label = stringResource(R.string.kelembapan),
                value = uiState.ioTDataOverviewDui.averageHumidityValue.getValue(context),
            )
        }

        Spacer(Modifier.height(16.dp))

        Row {

            IoTDataOverviewMenu(
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1.17f),
                iconResId = R.drawable.ic_sun,
                label = stringResource(R.string.intensitas_cahaya),
                value = uiState.ioTDataOverviewDui.averageLightIntensityValue.getValue(context),
            )
            Spacer(Modifier.width(16.dp))

            SeeMoreMenu(
                Modifier
                    .weight(1f)
                    .aspectRatio(1.17f),
                onClick = { navigateToSensorDataDetails(null, null) }
            )
        }

        Spacer(Modifier.height(40.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier
                    .width(21.dp)
                    .widthIn(min = 12.dp, max = 24.dp)
                    .aspectRatio(1.33f),
                imageVector = ImageVector
                    .vectorResource(R.drawable.ic_connectivity),
                contentDescription = null,
                tint = Orange85
            )
            Spacer(Modifier.width(10.dp))
            Text(
                stringResource(R.string.perangkat_tersambung),
                style = MaterialTheme.typography.headlineSmall,
                color = Black10
            )
        }

        Spacer(Modifier.height(16.dp))

        AddIoTDeviceSection(
            modifier = Modifier.fillMaxWidth(),
            onClick = onShowAddDeviceDialog
        )

        uiState.connectedDevices.forEach { iotDevice ->
            Spacer(Modifier.height(16.dp))

            IoTDeviceItem(
                ioTDevice = iotDevice,
                onMoreMenuClicked = {
                    onShowDeviceDetailDialog(iotDevice)
                },
                onClick = {
                    navigateToSensorDataDetails(iotDevice.id, iotDevice.name)
                }
            )
        }

        Spacer(Modifier.height(32.dp))

    }


    AddIoTDeviceDialog(
        isShownProvider = { uiState.isAddDeviceDialogShown },
        onDismiss = onDismissAddDeviceDialog,
        onSubmit = onSubmitAddDevice,
    )

    IoTDeviceDetailDialog(
        ioTDevice = uiState.selectedDeviceForDetailDialog,
        onDismiss = onDismissDeviceDetailDialog,
    )

}

@Preview
@Composable
private fun IoTDevicesInfoScreenPreview() {
    KakaoXpertTheme {
        IoTDevicesInfoScreen()
    }
}