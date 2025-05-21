package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.toplevel.diagnosishistory.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.neotelemetrixgdscunand.kakaoxpert.R
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Black10
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Green55
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Grey50
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Grey60
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.KakaoXpertTheme

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    queryProvider: () -> String = { "" },
    onQueryChange: (String) -> Unit = {},
    hint: String = "",
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    backgroundColor: Color = Color.White,
    onTextFieldTapped: () -> Unit = {}
) {

    val isSearchBarFocused by interactionSource.collectIsFocusedAsState()

    LaunchedEffect(isSearchBarFocused) {
        if (isSearchBarFocused) {
            onTextFieldTapped()
        }
    }

    val localModifier = remember {
        modifier
            .fillMaxWidth()
            .border(
                color = if (isSearchBarFocused) Green55 else Color.Transparent,
                width = 1.dp,
                shape = RoundedCornerShape(8.dp)
            )
            .background(shape = RoundedCornerShape(8.dp), color = backgroundColor)
            .padding(horizontal = 16.dp, vertical = 15.dp)
    }

    BasicTextField(
        modifier = localModifier,
        value = queryProvider(),
        onValueChange = onQueryChange,
        interactionSource = interactionSource,
        decorationBox = @Composable { innerTextField: @Composable () -> Unit ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    modifier = Modifier
                        .size(16.dp),
                    imageVector = ImageVector
                        .vectorResource(R.drawable.ic_search),
                    contentDescription = null,
                    tint = if (isSearchBarFocused) Black10 else Grey60
                )

                Spacer(Modifier.width(8.dp))

                if (!isSearchBarFocused && queryProvider().isEmpty()) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = hint,
                        style = MaterialTheme.typography.labelMedium,
                        color = Grey50
                    )
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                ) {
                    innerTextField()
                }

            }
        },
        textStyle = MaterialTheme.typography.labelMedium,
        maxLines = 1,
        singleLine = true,
    )
}

@Preview(showBackground = true)
@Composable
private fun SearchBarPreview() {
    KakaoXpertTheme {
        SearchBar()
    }

}