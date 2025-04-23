package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.toplevel.home.component

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.neotelemetrixgdscunand.kakaoxpert.R
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Black10
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Grey69
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.KakaoXpertTheme
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Orange85
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.util.ImagePainterStable

@Composable
fun IoTDataOverviewMenu(
    modifier: Modifier = Modifier,
    iconResId: Int,
    label: String,
    value:String = "",
    onClick: () -> Unit = {}
) {

    val imageModifier = remember {
        Modifier
            .clip(CircleShape)
            .border(width = 1.dp, color = Grey69, shape = CircleShape)
            .width(44.dp)
            .widthIn(32.dp, 64.dp)
            .aspectRatio(1f)
            .clickable(onClick = onClick)

    }

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        onClick = {},
        shape = RoundedCornerShape(8.dp),
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Box(
                modifier = imageModifier
            ) {
                ImagePainterStable(
                    modifier = Modifier
                        .align(Alignment.Center),
                    drawableResId = iconResId,
                    contentDescription = label
                )
            }

            Spacer(Modifier.height(16.dp))

            Text(
                text = label,
                style = MaterialTheme.typography.titleMedium,
                color = Black10,
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium,
                color = Orange85,
            )
        }
    }

}

@Preview(showBackground = true)
@Composable
private fun IoTOverviewMenuPreview() {
    KakaoXpertTheme {
        IoTDataOverviewMenu(
            modifier = Modifier,
            iconResId = R.drawable.ic_thermometer,
            label = "Suhu",
            value = "96%"
        )
    }
}