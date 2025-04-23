package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.auth.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Black10
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Green55
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Grey60
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.Grey70
import com.neotelemetrixgdscunand.kakaoxpert.presentation.theme.KakaoXpertTheme

@Composable
fun PrimaryTextField(
    modifier: Modifier = Modifier,
    isFocusedProvider: () -> Boolean = { false },
    valueProvider: () -> String = { "" },
    onValueChange: (String) -> Unit = {},
    hintText: String = "",
    textColor: Color = Black10,
    interactionSource: MutableInteractionSource = MutableInteractionSource(),
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
    visualTransformation: VisualTransformation = VisualTransformation.None,
    isBordered: Boolean = true,
    backgroundColor: Color = Color.Transparent,
    enabled: Boolean = true
) {

    val isFocused = isFocusedProvider()
    val value = valueProvider()

    val textFieldModifier = remember {
        modifier
            .fillMaxWidth()
            .then(
                if (isBordered) {
                    Modifier.border(
                        color = if (isFocused) Green55 else Grey70,
                        width = 1.dp,
                        shape = RoundedCornerShape(8.dp)
                    )
                } else {
                    Modifier
                }
            )
            .background(color = backgroundColor, shape = RoundedCornerShape(8.dp))
            .padding(contentPadding)
    }

    BasicTextField(
        modifier = textFieldModifier,
        value = value,
        onValueChange = onValueChange,
        visualTransformation = visualTransformation,
        interactionSource = interactionSource,
        enabled = enabled,
        decorationBox = @Composable { innerTextField: @Composable () -> Unit ->
            Row {
                if (leadingIcon != null) {
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 2.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        leadingIcon.invoke()
                    }
                }

                if (!isFocused && value.isEmpty()) {
                    Text(
                        text = hintText,
                        style = MaterialTheme.typography.labelMedium,
                        color = Grey60
                    )
                }
                Box(
                    modifier = Modifier.weight(1f)
                ) {
                    innerTextField()
                }

                if (trailingIcon != null) {
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 2.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        trailingIcon.invoke()
                    }
                }

            }
        },
        textStyle = MaterialTheme.typography.labelMedium.copy(
            color = textColor
        ),
        maxLines = 1,
        singleLine = true,
    )
}

@Preview(showBackground = true)
@Composable
private fun PrimaryTextFieldPreview() {
    KakaoXpertTheme {
        PrimaryTextField()
    }
}