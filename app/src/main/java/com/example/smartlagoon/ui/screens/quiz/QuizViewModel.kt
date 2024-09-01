package com.example.smartlagoon.ui.screens.quiz

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.common.reflect.TypeToken
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.PropertyName
import com.google.gson.Gson
import java.util.UUID

data class QuizQuestion(
    @get:PropertyName("question") @set:PropertyName("question") var question: String? = null,
    @get:PropertyName("options") @set:PropertyName("options") var options: List<String>? = null,
    @get:PropertyName("correctAnswerIndex") @set:PropertyName("correctAnswerIndex")  var correctAnswerIndex: Int,
    @get:PropertyName("points") @set:PropertyName("points") var points: Int,
    @get:PropertyName("completedBy") @set:PropertyName("completedBy") var completedBy: List<String>? = null
){
    // Costruttore senza argomenti è necessario per la deserializzazione
    constructor() : this(null, null, 0 ,0)
}

class QuizViewModel : ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val currentUser = auth.currentUser
    private val userId = currentUser?.uid

    private val _currentQuestionIndex = MutableLiveData(0)
    val currentQuestionIndex: LiveData<Int> = _currentQuestionIndex

    private val _currentQuestions = MutableLiveData<QuizQuestion>()
    val currentQuestion: LiveData<QuizQuestion> = _currentQuestions

    private val _questions = MutableLiveData<List<QuizQuestion>>(emptyList())
    val questions: LiveData<List<QuizQuestion>> = _questions

    private val _selectedOption = MutableLiveData<Int?>(null)
    val selectedOption: LiveData<Int?> = _selectedOption

    /*private val _totalQuestions = MutableLiveData(10) // Supponiamo ci siano 10 domande
    val totalQuestions: LiveData<Int> = _totalQuestions*/

    fun getUnconpletedQuestionsByUser() {
        firestore.collection("questions")
            /*.whereNotEqualTo(
                "completedBy",
                userId
            )*/ // Recupera quiz che non contengono l'ID dell'utente in `completedBy`
            .get()
            .addOnSuccessListener { result ->
                val incompleteQuestions = result.documents
                    .filter { !((it["completedBy"] as? List<*>)?.contains(userId) ?: false) }
                    .mapNotNull { it.toObject(QuizQuestion::class.java) }

                // Aggiorna LiveData con la lista delle sfide non completate
                _questions.postValue(incompleteQuestions)
                Log.d("incompleteQuestions", incompleteQuestions.toString())

                incompleteQuestions.forEach { question ->
                    Log.d("Firestore", "Incomplete question: ${question.question}")
                }
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Error getting incomplete challenges: ", exception)
            }
    }

    fun loadQuestionsFromJson(context: Context) {
        // Verifica se il database ha già le sfide caricate per evitare duplicati
        firestore.collection("questions").get().addOnSuccessListener { documents ->
            if (documents.isEmpty) {
                // Solo se non ci sono sfide esistenti nel database, carica da JSON
                val inputStream = context.assets.open("quiz_questions.json")
                val json = inputStream.bufferedReader().use { it.readText() }
                val quizQuestionsList: List<QuizQuestion> = Gson().fromJson(json, object : TypeToken<List<QuizQuestion>>() {}.type)

                // Aggiungi tutte le sfide al database
                quizQuestionsList.forEach { quizQuestion ->
                    addQuizQuestion(quizQuestion)
                }
            }
        }
    }


    private fun addQuizQuestion(quizQuestion: QuizQuestion) {

        val quiz = hashMapOf(
            "question" to quizQuestion.question,
            "options" to quizQuestion.options,
            "correctAnswerIndex" to quizQuestion.correctAnswerIndex,
            "points" to quizQuestion.points,
            "completedBy" to quizQuestion.completedBy,
        )

        // Aggiungi o aggiorna il documento dell'utente nella collezione "leaderboard"
        firestore.collection("questions").document(UUID.randomUUID().toString()).set(quiz)
            .addOnSuccessListener {
                println("Quiz aggiunta con successo " +  quizQuestion.question +"!")
            }
            .addOnFailureListener { e ->
                println("Errore nell'aggiunta della quiz per  " +  quizQuestion.question + ": $e")
            }
    }


    fun selectOption(index: Int) {
        _selectedOption.value = index
    }

    fun nextQuestion() {
        val currentIndex = _currentQuestionIndex.value ?: 0
        val questionList = _questions.value ?: emptyList()
        _selectedOption.value = null

        if (currentIndex < questionList.size - 1) {
            _currentQuestionIndex.value = currentIndex + 1
            _currentQuestions.value = questionList[currentIndex + 1]
        } else {
            // Logica per gestire la fine del quiz, ad esempio mostrare un messaggio o navigare altrove
            Log.d("QuizViewModel", "Quiz completato")
        }

    }
}
