package com.example.smartlagoon.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import com.example.smartlagoon.R

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

val myCustomFont = FontFamily(
    Font(R.font.gloria_hallelujah_regular, FontWeight.Normal)
)

val AppTypography = Typography(
    bodyLarge = TextStyle(
        fontFamily = myCustomFont,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    titleLarge = TextStyle(
        fontFamily = myCustomFont,
        fontWeight = FontWeight.Bold,
        fontSize = 21.sp
    ),
    labelLarge = TextStyle(
        fontFamily = myCustomFont,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp
    ),
)

@Composable
fun SmartlagoonTheme(
    content: @Composable () -> Unit
) {
    val colorScheme= LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primaryContainer.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}
