package com.example.smartlagoon.ui.screens.about

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.smartlagoon.R
import com.example.smartlagoon.ui.composables.TopAppBar

@Composable
fun AboutScreen(
    navController: NavHostController,
) {
    val uriHandler = LocalUriHandler.current
    Scaffold(
        topBar = {
            TopAppBar(
                navController = navController,
                currentRoute = "About"
            )
        },
        bottomBar = {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .border(1.dp, MaterialTheme.colorScheme.primaryContainer, RectangleShape)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.lagoonguard_logo),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                )
            }
        }
    ) { contentPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Text(
                    text = "Informazioni sul Progetto Europeo Smartlagoon",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Smartlagoon è un progetto innovativo finanziato dall'Unione Europea, volto a promuovere la sostenibilità e l'efficienza nella gestione delle risorse idriche. Il progetto mira a sviluppare soluzioni tecnologiche avanzate per migliorare la qualità dell'acqua e la gestione dei laghi e delle zone umide in Europa.",
                    fontSize = 16.sp
                )
            }
            item {
                Text(
                    text = "Obiettivi del Progetto",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "1. Monitoraggio Ambientale: Implementare tecnologie all'avanguardia per il monitoraggio continuo della qualità dell'acqua e delle condizioni ambientali nei laghi e nelle zone umide.\n\n" +
                            "2. Analisi Dati: Utilizzare strumenti di analisi dei dati per prevedere e prevenire situazioni critiche come l'eutrofizzazione e l'inquinamento.\n\n" +
                            "3. Gestione Sostenibile: Sviluppare strategie e tecniche di gestione sostenibile per preservare e migliorare gli ecosistemi acquatici.\n\n" +
                            "4. Innovazione Tecnologica: Creare e testare nuove tecnologie e metodi per il monitoraggio e la gestione delle risorse idriche, con un focus particolare su soluzioni eco-compatibili.",
                    fontSize = 16.sp
                )
            }
            item {
                Text(
                    text = "Attività e Risultati",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "• Sviluppo di Sensori Avanzati: Progettazione e installazione di sensori per il monitoraggio in tempo reale della qualità dell'acqua e delle condizioni ambientali.\n\n" +
                            "• Piattaforme di Dati: Creazione di piattaforme digitali per la raccolta, l'analisi e la visualizzazione dei dati ambientali.\n\n" +
                            "• Formazione e Sensibilizzazione: Attività di formazione per le autorità locali e le comunità su come utilizzare le tecnologie sviluppate e su best practices per la gestione sostenibile delle risorse idriche.\n\n" +
                            "• Collaborazione Internazionale: Il progetto coinvolge partner di diversi paesi europei, lavorando insieme per affrontare le sfide ambientali comuni e condividere conoscenze e tecnologie.",
                    fontSize = 16.sp
                )
            }
            item {
                Text(
                    text = "Impatto",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Smartlagoon ha un impatto significativo sul miglioramento della gestione delle risorse idriche in Europa, contribuendo a:\n\n" +
                            "• Migliorare la qualità dell'acqua nei laghi e nelle zone umide.\n\n" +
                            "• Promuovere la sostenibilità ambientale attraverso l'uso di tecnologie innovative.\n\n" +
                            "• Aumentare la consapevolezza e le competenze locali nella gestione delle risorse idriche.",
                    fontSize = 16.sp
                )
            }
            /*item {
                Text(
                    text = "Per Maggiori Informazioni",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Per ulteriori dettagli sul progetto Smartlagoon, visita il sito web ufficiale del progetto o contatta i partner di progetto:\n\n" +
                            "• Sito Web del Progetto: (http://www.smartlagoon.eu)\n\n" +
                            "• Contatti: info@smartlagoon.eu",
                    fontSize = 16.sp
                )
            }*/
            item {
                Text(
                    text = "Per Maggiori Informazioni",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Row for Project Website
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Per ulteriori dettagli sul progetto Smartlagoon, visita il sito web ufficiale del progetto o contatta i partner di progetto:\n\n" +
                                "• Sito Web del Progetto: ",
                        fontSize = 16.sp
                    )
                }

                ClickableText(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(color = Color.Blue, textDecoration = TextDecoration.Underline)) {
                            pushStringAnnotation(tag = "URL", annotation = "http://www.smartlagoon.eu")
                            append("www.smartlagoon.eu")
                        }
                    },
                    onClick = { offset ->
                        val annotatedString = buildAnnotatedString {
                            withStyle(style = SpanStyle(color = Color.Blue, textDecoration = TextDecoration.Underline)) {
                                pushStringAnnotation(tag = "URL", annotation = "http://www.smartlagoon.eu")
                                append("www.smartlagoon.eu")
                            }
                        }
                        val annotation = annotatedString.getStringAnnotations("URL", offset, offset).firstOrNull()
                        annotation?.let {
                            uriHandler.openUri(it.item)
                        }
                    }
                )

                // Row for Contact Email
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "• Contatti: ",
                        fontSize = 16.sp
                    )
                    ClickableText(
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle(color = Color.Blue, textDecoration = TextDecoration.Underline)) {
                                pushStringAnnotation(tag = "EMAIL", annotation = "mailto:info@smartlagoon.eu")
                                append("info@smartlagoon.eu")
                            }
                        },
                        onClick = { offset ->
                            val annotatedString = buildAnnotatedString {
                                withStyle(style = SpanStyle(color = Color.Blue, textDecoration = TextDecoration.Underline)) {
                                    pushStringAnnotation(tag = "EMAIL", annotation = "mailto:info@smartlagoon.eu")
                                    append("info@smartlagoon.eu")
                                }
                            }
                            val annotation = annotatedString.getStringAnnotations("EMAIL", offset, offset).firstOrNull()
                            annotation?.let {
                                uriHandler.openUri(it.item)
                            }
                        }
                    )
                }
            }
        }
    }
}


