package com.example.smartlagoon.ui.composables

import android.view.MotionEvent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.unit.dp
import com.example.smartlagoon.ui.theme.myButtonColors

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AnimatedButton(text: String) {
    var isPressed by remember { mutableStateOf(false) }

    // Animazione di scaling
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.9f else 1f,  // Riduce la dimensione del bottone al 90% quando premuto
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ), label = ""
    )

    Button(
        onClick = { /* Azione del bottone */ },
        modifier = Modifier
            .padding(16.dp)
            .scale(scale)  // Applica l'animazione di scaling
            .pointerInteropFilter {
                when (it.action) {
                    MotionEvent.ACTION_DOWN -> {
                        isPressed = true
                    }
                    MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                        isPressed = false
                    }
                }
                true // Modifica qui: ritorna true per indicare che l'evento è gestito
            }
            .fillMaxWidth(),
        colors = myButtonColors()
    ) {
        Text(text)
    }
}