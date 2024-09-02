package com.example.smartlagoon.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
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
    primary = Color.Black,
    onPrimary = Color.Black,
    primaryContainer = MyColors().myBlu,
    onPrimaryContainer = Color.White,
    secondaryContainer = MyColors().myGreen,
    onSecondaryContainer = Color.White,
    tertiaryContainer = MyColors().backgroundLightBlue,
    onTertiaryContainer = MyColors().borders,
    secondary = Color.Black,
    tertiary = Color.Black,
    background = Color.DarkGray,
    onBackground = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = Color.Black,
    onPrimary = Color.Black,
    primaryContainer = MyColors().myBlu,
    onPrimaryContainer = Color.White,
    secondaryContainer = MyColors().myGreen,
    onSecondaryContainer = Color.White,
    tertiaryContainer = MyColors().backgroundLightBlue,
    onTertiaryContainer = MyColors().borders,
    secondary = Color.Black,
    tertiary = Color.Black,
    background = Color.White,
    onBackground = Color.Black
)

@Composable
fun myButtonColors(): ButtonColors {
    return ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
    )
}

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
