package com.example.smartlagoon.ui.screens.quiz

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import com.example.smartlagoon.R
import com.example.smartlagoon.ui.composables.TopAppBar
import com.example.smartlagoon.ui.theme.myButtonColors
import com.example.smartlagoon.ui.viewmodel.UsersDbViewModel

@Composable
fun QuizScreen(
    quizVm: QuizViewModel,
    usersDbVm: UsersDbViewModel,
    navController: NavHostController
) {
    val ctx = LocalContext.current
    val questionIndex by quizVm.currentQuestionIndex.observeAsState(0)
    val questionList by quizVm.questions.observeAsState(emptyList())
    val selectedOption by quizVm.selectedOption.observeAsState()

    // Carica la domanda corrente
    val currentQuestion = questionList.getOrNull(questionIndex)

    // Stato per l'AlertDialog
    val showDialog = remember { mutableStateOf(false) }
    val dialogMessage = remember { mutableStateOf("") }

    val user by usersDbVm.userLiveData.observeAsState()

    // Funzione per mostrare l'AlertDialog
    fun showMessageDialog(message: String) {
        dialogMessage.value = message
        showDialog.value = true
    }

    // Mostra l'AlertDialog se necessario
    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = { Text(text = "Informazione") },
            text = { Text(text = dialogMessage.value) },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDialog.value = false
                        quizVm.nextQuestion()
                    }
                ) {
                    Text("OK")
                }
            },
            properties = DialogProperties(dismissOnClickOutside = false)
        )
    }

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
        if (questionList.isNotEmpty() && questionIndex < questionList.size) {
            Box(
                modifier = Modifier
                    .padding(contentPadding)
                    .fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxSize()
                ) {
                    currentQuestion?.let {
                        it.question?.let { it1 ->
                            Text(
                                text = it1,
                                style = MaterialTheme.typography.headlineMedium,
                                modifier = Modifier.padding(bottom = 16.dp),
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                        Log.d("it", it.toString())

                        it.category?.let { it1 ->
                            Text(
                                text = it1,
                                style = MaterialTheme.typography.titleSmall,
                                modifier = Modifier.padding(bottom = 6.dp),
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }

                        it.options?.forEachIndexed { index, option ->
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
                                        showMessageDialog("Risposta corretta!\nHai guadagnato ${it.points} punti!")
                                        usersDbVm.addPoints(it.points)
                                        //usersDbVm.fetchUserProfile()
                                    } else {
                                        showMessageDialog(
                                            "Risposta errata. La risposta corretta era: ${
                                                it.options?.get(
                                                    correctAnswerIndex
                                                )
                                            }"
                                        )
                                    }
                                } else {
                                    Toast.makeText(
                                        ctx,
                                        "Seleziona una risposta prima di continuare",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                Log.d("questionDone", "done1")
                                quizVm.quizQuestionDone()
                                Log.d("questionDone", "done3")
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            colors = myButtonColors()
                        ) {
                            Text("Prossima domanda")
                        }

                        Text(
                            text = "I tuoi punti: ${user?.points}"
                        )
                    }
                }
            }
        } else {
            Log.d("DOmande finite", "domande finite")
            Box(
                modifier = Modifier
                    .padding(contentPadding)
                    .fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxSize()
                ) {
                    Text(
                        text = "Domande terminate, presto ne saranno aggiunte delle altre!",
                        color = Color.Black,
                    )
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
            containerColor = if (isSelected) MaterialTheme.colorScheme.tertiaryContainer else MaterialTheme.colorScheme.surface
        ),
        shape = MaterialTheme.shapes.medium
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = if (isSelected) Color.Red else MaterialTheme.colorScheme.onSurface
        )
    }
}
