package com.neotelemetrixgdscunand.kakaoxpert.presentation.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Green55,
    secondary = Color.Transparent,
    tertiary = Pink80,
    onPrimary = Color.White,
    onSecondary = Green55,
)

private val LightColorScheme = lightColorScheme(
    primary = Green55,
    secondary = Color.Transparent,
    onPrimary = Color.White,
    onSecondary = Green55,
    surface = Grey90,
    onSurface = Color.Black,
    onSurfaceVariant = Grey60,
    primaryContainer = Color.White,
    onPrimaryContainer = Black10,
    secondaryContainer = Green60,
    onSecondaryContainer = Color.White,
    tertiaryContainer = Orange90,
    onTertiaryContainer = Green55,
    outline = Grey70,
    outlineVariant = Green55

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun KakaoXpertTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}