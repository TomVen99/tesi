package com.example.smartlagoon.ui.screens.quiz

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

data class QuizQuestion(
    val question: String,
    val options: List<String>,
    val correctAnswerIndex: Int,
    val points: Int
)

class QuizViewModel : ViewModel() {
    private val _currentQuestionIndex = MutableLiveData(0)
    val currentQuestionIndex: LiveData<Int> = _currentQuestionIndex

    private val _currentQuestions = MutableLiveData<QuizQuestion>()
    val currentQuestion: LiveData<QuizQuestion> = _currentQuestions

    private val _questions = MutableLiveData<List<QuizQuestion>>(emptyList())
    val questions: LiveData<List<QuizQuestion>> = _questions

    private val _selectedOption = MutableLiveData<Int?>(null)
    val selectedOption: LiveData<Int?> = _selectedOption

    private val _totalQuestions = MutableLiveData(10) // Supponiamo ci siano 10 domande
    val totalQuestions: LiveData<Int> = _totalQuestions

    fun loadQuestions() {
        // Carica le domande del quiz, ad esempio da una rete o database locale
        _questions.value = listOf(
            QuizQuestion("Qual è la capitale dell'Italia?", listOf("Roma", "Milano", "Napoli", "Torino"), 0, 10),
            QuizQuestion("Qual è il colore del cielo?", listOf("Blu", "Verde", "Rosso", "Giallo"), 0, 20)
        )
    }

    fun selectOption(index: Int) {
        _selectedOption.value = index
    }

    fun nextQuestion() {
        val currentIndex = _currentQuestionIndex.value ?: 0
        val newIndex = currentIndex + 1
        _currentQuestionIndex.value = newIndex

        val total = _totalQuestions.value ?: 1

        /*_currentQuestionIndex.value = (_currentQuestionIndex.value!! + 1) % (_questions.value?.size ?: 1)
        _selectedOption.value = null*/
    }
}
