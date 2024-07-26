package com.example.smartlagoon.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = Color.White,
    onPrimary = Color.White,
    primaryContainer = DarkBLue,
    onPrimaryContainer = Color.White,
    secondaryContainer = Color.Yellow,
    onSecondaryContainer = Color.Black,
    tertiaryContainer = UltraLightBlue,
    onTertiaryContainer = Color.Black,
    secondary = DarkBLue,
    tertiary = DarkBLue,
    background = Color.DarkGray,
    onBackground = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = Color.Black,
    onPrimary = Color.Black,
    primaryContainer = MyBlu,
    onPrimaryContainer = Color.White,
    secondaryContainer = MyGreen,
    onSecondaryContainer = Color.White,
    tertiaryContainer = BackgroundLightBlue,
    onTertiaryContainer = Borders,
    secondary = Color.Black,
    tertiary = Color.Black,
    background = Color.White,
    onBackground = Color.Black
)

@Composable
fun SmartlagoonTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primaryContainer.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

enum class Theme { Light, Dark, System }
