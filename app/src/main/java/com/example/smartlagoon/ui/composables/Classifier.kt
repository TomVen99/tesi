package com.example.smartlagoon.ui.composables

import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.smartlagoon.utils.MarineClassifier

@Composable
fun MarineClassificationScreen() {
    val ctx = LocalContext.current
    val classifier = remember { MarineClassifier(ctx) }
    var classificationResult by remember { mutableStateOf<Int?>(null) }

    //Selezione immagine
    // Stato per immagazzinare l'immagine selezionata
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    // Contesto dell'activity corrente
    val context = LocalContext.current

    // Launcher per lanciare l'intento di selezione dell'immagine
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        // Quando l'utente seleziona un'immagine, questo codice viene eseguito
        selectedImageUri = uri
        Log.d("uri", selectedImageUri.toString())
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = {
            launcher.launch("image/*")
            // Logica per scegliere un'immagine
        }) {
            Text(text = "Scegli un'immagine")
        }

        selectedImageUri?.let { uri ->
            val bitmap = MediaStore.Images.Media.getBitmap(ctx.contentResolver, uri)
            classificationResult = classifier.classifyImage(bitmap)
        }

        classificationResult?.let {
            Log.d("result", MarineClassifier.Categories.entries[it].toString())
            Text(text = "Risultato della classificazione: ${MarineClassifier.Categories.entries[it]}")
        }
    }
}
