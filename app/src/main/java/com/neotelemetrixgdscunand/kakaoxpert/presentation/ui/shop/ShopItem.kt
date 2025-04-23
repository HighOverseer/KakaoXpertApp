package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.shop

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.neotelemetrixgdscunand.kakaoxpert.R
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Black10
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Grey47
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.KakaoXpertTheme
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Green55
import com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.util.AsyncImagePainterStable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShopItem(
    modifier: Modifier = Modifier,
    imageUrl: String = "",
    title: String = "Pestisida Fungsida Inteksida",
    price: String = "Rp 205.000",
    targetUrl: String = ""
) {

    val configuration = LocalConfiguration.current
    val cardModifier = remember {
        val imageToParentAspectRatio = 0.44f
        val screenWidth = configuration.screenWidthDp
        val sizeMin = (screenWidth * imageToParentAspectRatio).dp
        modifier
            .background(Color.White, shape = RoundedCornerShape(12.dp))
            .padding(top = 8.dp, start = 8.dp, end = 8.dp, bottom = 16.dp)
            .width(sizeMin)
    }

    val context = LocalContext.current

    val onClick = remember {
        {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(targetUrl))
            context.startActivity(intent)
        }
    }
    Card(
        shape = RoundedCornerShape(0.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ),
        modifier = cardModifier,
        onClick = onClick
    ) {

        val imageModifier = remember {
            Modifier
                .clip(RoundedCornerShape(8.dp))
                .fillMaxWidth()
                .aspectRatio(1.11f)
        }
        Box(
            modifier = imageModifier,
            contentAlignment = Alignment.Center
        ) {
            AsyncImagePainterStable(
                modifier = Modifier.align(Alignment.Center),
                imageUrlOrPath = imageUrl,
                placeholderResId = R.drawable.ic_camera,
                contentScale = ContentScale.Crop,
                contentDescription = null
            )
        }

        Spacer(Modifier.height(16.dp))

        Text(
            title,
            style = MaterialTheme.typography.titleMedium,
            color = Black10,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(Modifier.height(16.dp))

        Row(Modifier.fillMaxWidth()) {
            Text(
                text = price,
                style = MaterialTheme.typography.labelMedium,
                color = Green55
            )

            Spacer(modifier = Modifier.weight(1f))

            Icon(
                imageVector = ImageVector.vectorResource(
                    R.drawable.ic_right_arrow
                ),
                tint = Grey47,
                contentDescription = null
            )
        }

    }

}


@Preview
@Composable
private fun ShopItemPreview() {
    KakaoXpertTheme {
        ShopItem(
            imageUrl = "https://s3-alpha-sig.figma.com/img/37c1/c191/518ba1e52a545e70e67c846bb71fad76?Expires=1732492800&Key-Pair-Id=APKAQ4GOSFWCVNEHN3O4&Signature=FXbsgEn4bIqA7qdVEN7fM5B0P7M5Cw39Kl4z86CBFrxnNUQnCGs~1ErZnqFJArACM9GxtNG~GkmBzCFTp2O9fzSDcR2qT0Av2zIYoSa7Zd9inOW51ZOsVjO1FdxLqeWEPJNSOqWEYGgfkr~hcmF07o5IZCX36ta1rLb58CB3BuLV54L~2UXq3n2HDrwwepsAQ-8erapfhCqIfagNTmsWbqyXZErCqJ8jjsEgeGGjAga8DZhAHle4LXwAi25Blmcofv-83fkacg0WEzEM4aNKCH6V0upOfrPPpoRliQuxJOJsvHkoGjh6x07qlPLCfq1~ZIB4IXEm937RyaW-I7Rnqg__"
        )
    }
}