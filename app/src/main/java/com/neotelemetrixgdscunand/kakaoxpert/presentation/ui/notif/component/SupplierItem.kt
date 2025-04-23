package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.notif.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.neotelemetrixgdscunand.kakaoxpert.R
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Black10
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Grey60
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.KakaoXpertTheme
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.util.AsyncImagePainterStable

@Composable
fun SupplierItem(
    modifier: Modifier = Modifier,
    supplierName: String = "",
    supplierImageUrl: String = ""
) {
    Row(
        modifier = modifier, verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImagePainterStable(
            imageUrlOrPath = supplierImageUrl,
            placeholderResId = R.drawable.ic_camera,
            contentScale = ContentScale.Crop,
            contentDescription = null,
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
        )

        Spacer(Modifier.width(16.dp))

        Text(
            supplierName,
            style = MaterialTheme.typography.labelMedium.copy(
                lineHeight = 15.sp,
                letterSpacing = (-0.02).sp
            ),
            color = Black10,
            modifier = Modifier.weight(1f)
        )

        Text(
            "Menyanggupi 10 Ton",
            style = MaterialTheme.typography.labelMedium.copy(
                lineHeight = 15.sp,
                letterSpacing = (-0.02).sp
            ),
            color = Grey60
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SupplierItemPreview() {
    KakaoXpertTheme {
        SupplierItem()
    }
}