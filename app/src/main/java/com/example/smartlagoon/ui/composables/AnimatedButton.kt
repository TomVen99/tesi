package com.example.smartlagoon.ui.composables

import android.content.Intent
import android.view.MotionEvent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.smartlagoon.R
import com.example.smartlagoon.TakePhotoActivity
import com.example.smartlagoon.data.database.Challenge
import com.example.smartlagoon.ui.SmartlagoonRoute
import com.example.smartlagoon.ui.theme.myButtonColors

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AnimatedButton(challenge: Challenge) {
    var isPressed by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current  // Ottieni il Context corrente

    // Animazione di scaling
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.9f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ), label = ""
    )

    Button(
        onClick = {
            //onClick() // Esegui l'azione associata al click
            showDialog = true // Mostra il popup quando si rilascia il bottone
        },
        modifier = Modifier
            .padding(16.dp)
            .scale(scale)
            /*.pointerInteropFilter {
                when (it.action) {
                    MotionEvent.ACTION_DOWN -> {
                        isPressed = true
                    }
                    MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                        isPressed = false
                    }
                }
                true
            }*/
            .fillMaxWidth(),
        colors = myButtonColors()
    ) {
        Text(challenge.title)
    }

    // Popup Dialog
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = {
                Text(text = challenge.title)
            },
            text = {
                Column {
                    Image(
                        painter = painterResource(id = R.drawable.ic_launcher_foreground),  // Sostituisci `your_image` con il nome dell'immagine nella cartella drawable
                        contentDescription = null,
                        modifier = Modifier.size(40.dp)  // Imposta la dimensione dell'icona
                    )
                    Text(text = challenge.description)
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showDialog = false // Chiude il popup
                        val intent = Intent(context, TakePhotoActivity::class.java).apply {
                            putExtra("challengePoints", challenge.points)
                        }
                        context.startActivity(intent)
                    },
                    colors = myButtonColors(),
                ) {
                    Text("Scatta + " +  challenge.points.toString() + " punti")
                }
            }
        )
    }
}