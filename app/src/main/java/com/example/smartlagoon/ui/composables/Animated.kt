package com.example.smartlagoon.ui.composables

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.smartlagoon.R

/*@Composable
fun AnimatedBackground() {
    // Carica l'animazione Lottie dal file JSON
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.mare))

    // Avvia l'animazione automaticamente
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever
    )

    // Visualizza l'animazione come sfondo
    LottieAnimation(
        composition,
        progress,
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
    )
}*/

@Composable
fun AnimatedImage(resId: Int) {
    // Carica l'animazione Lottie dal file JSON
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(resId))

    // Avvia l'animazione automaticamente
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever
    )

    // Visualizza l'animazione come sfondo
    LottieAnimation(
        composition,
        progress,
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
    )
}