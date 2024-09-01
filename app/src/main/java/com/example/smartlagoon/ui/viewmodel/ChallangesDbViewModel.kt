package com.example.smartlagoon.ui.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartlagoon.data.repositories.ChallengeRepository
import com.google.common.reflect.TypeToken
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.PropertyName
import com.google.gson.Gson
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.InputStreamReader
import java.util.UUID

data class ChallengesDbState(val challenges: List<Challenge>)

class ChallengesDbViewModel() : ViewModel() {

    private val _userUncompletedChallenges = MutableLiveData<List<Challenge>>()
    var userUncompleteChallenges: LiveData<List<Challenge>> = _userUncompletedChallenges

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val currentUser = auth.currentUser
    private val userId = currentUser?.uid
    fun getUnconpletedChallengeByUser() {
        Log.d("chDBVM", userId.toString())
        firestore.collection("challenges")
            /*.whereNotEqualTo(
                "completedBy",
                userId
            )*/ // Recupera sfide che non contengono l'ID dell'utente in `completedBy`
            .get()
            .addOnSuccessListener { result ->
                val incompleteChallenges = result.documents
                    .filter { !((it["completedBy"] as? List<*>)?.contains(userId) ?: false) }
                    .mapNotNull { it.toObject(Challenge::class.java) }

                // Aggiorna LiveData con la lista delle sfide non completate
                _userUncompletedChallenges.postValue(incompleteChallenges)

                incompleteChallenges.forEach { challenge ->
                    Log.d("Firestore", "Incomplete challenge: ${challenge.title}")
                }
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Error getting incomplete challenges: ", exception)
            }
    }

    /*fun getAllChallenges() = viewModelScope.launch {
        repository.getAllChallenges()
    }

    fun getUncompletedChallengesForUser(username: String) = viewModelScope.launch {
        val userUncompletedChallenges = repository.getUncompletedChallengesForUser(username)
        _userUncompletedChallenges.value = userUncompletedChallenges
    }*/

    fun createChallangeTest() = viewModelScope.launch {
        //  repository.generateChallengeTest()
    }

    // Funzione per aggiungere un punteggio alla collezione "leaderboard"
    private fun addChallenge(challenge: Challenge) {

        val userScore = hashMapOf(
            "title" to challenge.title,
            "description" to challenge.description,
            "points" to challenge.points,
            "completedBy" to challenge.completedBy,
        )

        // Aggiungi o aggiorna il documento dell'utente nella collezione "leaderboard"
        firestore.collection("challenges").document(UUID.randomUUID().toString()).set(userScore)
            .addOnSuccessListener {
                println("Challenge aggiunta con successo " +  challenge.title +"!")
            }
            .addOnFailureListener { e ->
                println("Errore nell'aggiunta della challenge per  " +  challenge.title + ": $e")
            }
    }

    fun loadChallengesFromJson(context: Context) {
        // Verifica se il database ha già le sfide caricate per evitare duplicati
        firestore.collection("challenges").get().addOnSuccessListener { documents ->
            if (documents.isEmpty) {
                // Solo se non ci sono sfide esistenti nel database, carica da JSON
                val inputStream = context.assets.open("challenges.json")
                val json = inputStream.bufferedReader().use { it.readText() }
                val challengesList: List<Challenge> = Gson().fromJson(json, object : TypeToken<List<Challenge>>() {}.type)

                // Aggiungi tutte le sfide al database
                challengesList.forEach { challenge ->
                    addChallenge(challenge)
                }
            }
        }
    }
}

data class Challenge(
    @get:PropertyName("title") @set:PropertyName("title") var title: String? = null,
    @get:PropertyName("description") @set:PropertyName("description") var description: String? = null,
    @get:PropertyName("points") @set:PropertyName("points") var points: Int,
    @get:PropertyName("completedBy") @set:PropertyName("completedBy") var completedBy: List<String>? = null
) {
    // Costruttore senza argomenti è necessario per la deserializzazione
    constructor() : this(null, null, 0 ,null)
}