package com.example.smartlagoon.ui.screens.quiz

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.smartlagoon.R
import com.example.smartlagoon.ui.composables.TopAppBar
import com.example.smartlagoon.ui.theme.myButtonColors
import com.example.smartlagoon.ui.viewmodel.UsersDbViewModel


/*@Composable
fun QuizScreen(
    quizVm: QuizViewModel,
    usersDbVm: UsersDbViewModel,
    navController: NavHostController
) {
    val currentQuestionIndex by quizVm.currentQuestionIndex.observeAsState(0)
    val questions by quizVm.questions.observeAsState(emptyList())
    val selectedOption by quizVm.selectedOption.observeAsState(null)

    val question = questions.getOrNull(currentQuestionIndex)

    Scaffold(
        topBar = {
            TopAppBar(
                navController = navController,
                currentRoute = "Quiz"
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
                    painter = painterResource(id = R.drawable.lagoonguard_logo),//smartlagoon_logo),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                )
            }
        }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize()
        ) {
            question?.let {
                Text(
                    text = it.question + " " + it.points + " punti",
                    modifier = Modifier.padding(16.dp).fillMaxWidth()
                )

                it.options.forEachIndexed { index, option ->
                    Button(
                        onClick = { quizVm.selectOption(index) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            containerColor =  if (selectedOption == index) Color.Gray else MaterialTheme.colorScheme.secondaryContainer
                        )
                    ) {
                        Text(text = option)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                val ctx = LocalContext.current
                Button(
                    onClick = {
                        if (selectedOption != null) {
                            // Verifica se la risposta è corretta
                            val correctAnswerIndex = it.correctAnswerIndex
                            val isCorrect = selectedOption == correctAnswerIndex
                            // Mostra feedback all'utente e carica la prossima domanda
                            if (isCorrect) {
                                Toast.makeText(ctx, "Risposta corretta!\nHai guadagnato ${it.points} punti!", Toast.LENGTH_SHORT).show()
                                usersDbVm.addPoints(it.points)
                            } else {
                                Toast.makeText(ctx, "Risposta errata. La risposta corretta era: ${it.options[correctAnswerIndex]}", Toast.LENGTH_LONG).show()
                            }
                            quizVm.nextQuestion()
                        }
                    },
                    colors = myButtonColors(),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Prossima domanda")
                }
            }
        }
    }
}
*/
@Composable
fun QuizScreen(
    quizVm: QuizViewModel,
    usersDbVm: UsersDbViewModel,
    navController: NavHostController
) {
    val context = LocalContext.current
    val question by quizVm.currentQuestion.observeAsState()
    val selectedOption by quizVm.selectedOption.observeAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                navController = navController,
                currentRoute = "Quiz"
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
                    painter = painterResource(id = R.drawable.lagoonguard_logo),//smartlagoon_logo),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                )
            }
        }
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize()
                .background(brush = Brush.verticalGradient(listOf(Color.Blue, Color.Cyan)))
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize()
            ) {
                question?.let {
                    Text(
                        text = it.question,
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(bottom = 16.dp),
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    it.options.forEachIndexed { index, option ->
                        OptionButton(
                            text = option,
                            isSelected = selectedOption == index,
                            onClick = { quizVm.selectOption(index) }
                        )
                    }

                    Button(
                        onClick = {
                            if (selectedOption != null) {
                                val correctAnswerIndex = it.correctAnswerIndex
                                val isCorrect = selectedOption == correctAnswerIndex
                                if (isCorrect) {
                                    Toast.makeText(
                                        context,
                                        "Risposta corretta!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    Toast.makeText(
                                        context,
                                        "Risposta errata. La risposta corretta era: ${it.options[correctAnswerIndex]}",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                                quizVm.nextQuestion()
                            } else {
                                Toast.makeText(
                                    context,
                                    "Seleziona una risposta prima di continuare",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary
                        )
                    ) {
                        Text("Prossima domanda")
                    }
                }
            }
        }
    }
}


@Composable
fun OptionButton(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
        ),
        shape = MaterialTheme.shapes.medium
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
        )
    }
}
