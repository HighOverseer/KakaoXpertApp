package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.toplevel.iotdevicesinfo.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Green55
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Grey69
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.KakaoXpertTheme
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.util.dashedBorder

@Composable
fun AddIoTDeviceSection(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    val cardModifier = remember {
        modifier
            .background(
                color = Color.Transparent,
                shape = RoundedCornerShape(8.dp)
            )
            .dashedBorder(
                color = Grey69,
                shape = RoundedCornerShape(8.dp),
                strokeWidth = 1.dp,
                gapLength = 16.dp,
                dashLength = 16.dp
            )
    }

    Card(
        onClick = onClick,
        modifier = cardModifier,
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent,
            contentColor = Color.Transparent
        ),
    ) {

        Row(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {


            Icon(
                modifier = Modifier
                    .size(14.dp),
                imageVector = ImageVector
                    .vectorResource(R.drawable.ic_add),
                contentDescription = null,
                tint = Green55
            )

            Spacer(Modifier.width(8.dp))

            Text(
                text = stringResource(R.string.tambah_perangkat),
                style = MaterialTheme.typography.titleMedium,
                color = Green55
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
private fun AddIoTDeviceSectionPreview() {
    KakaoXpertTheme {
        AddIoTDeviceSection()
    }
}