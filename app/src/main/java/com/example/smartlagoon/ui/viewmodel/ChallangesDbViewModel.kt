package com.example.smartlagoon.ui.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.smartlagoon.data.repository.UserRepository
import com.example.smartlagoon.ui.screens.quiz.QuizQuestion
import com.google.common.reflect.TypeToken
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.PropertyName
import com.google.gson.Gson
import java.util.UUID

class ChallengesDbViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _allChallenges = MutableLiveData<List<Challenge>>()
    var allChallenges: LiveData<List<Challenge>> = _allChallenges

    private val firestore = FirebaseFirestore.getInstance()
    val currentUser: LiveData<FirebaseUser?> = userRepository.currentUser


    private val _currentChallenge = MutableLiveData<Challenge?>()
    var currentChallenge: LiveData<Challenge?> = _currentChallenge
    fun setCurrentChallenge(challenge: Challenge?) {
        _currentChallenge.value = challenge
        Log.d("currentChallengeChallenge", challenge.toString())
    }

    fun getAllChallenges() {
        firestore.collection("challenges")
            .get()
            .addOnSuccessListener { result ->
                val allChallenges = result.documents
                    .mapNotNull { documentSnapshot ->
                        val challenge = documentSnapshot.toObject(Challenge::class.java)
                        challenge?.id = documentSnapshot.id
                        challenge
                    }

                _allChallenges.postValue(allChallenges)

                allChallenges.forEach { challenge ->
                    Log.d("Firestore", "Incomplete challenge: ${challenge.id}")
                }
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Error getting incomplete challenges: ", exception)
            }
    }
    fun challengeDone(challengeId: String) {
        val userUUID = currentUser.value?.uid ?: return

        Log.d("challengeId", challengeId)
        val questionRef = firestore.collection("challenges").document(challengeId)

        Log.d("challengeDone", challengeId)
        firestore.runTransaction { transaction ->
            val snapshot = transaction.get(questionRef)
            val completedBy = snapshot.get("completedBy") as? MutableList<String> ?: mutableListOf()

            if (!completedBy.contains(userUUID)) {
                completedBy.add(userUUID)
                transaction.update(questionRef, "completedBy", completedBy)
            }
            Log.d("questionDone","done2")
        }.addOnSuccessListener {
            Log.d("questionDone","doneaaaa")
            Log.d("QuizViewModel", "L'UUID dell'utente è stato aggiunto con successo a $challengeId")
        }.addOnFailureListener { e ->
            Log.e("QuizViewModel", "Errore durante l'aggiornamento del documento: ", e)
        }
    }

    private fun addChallenge(challenge: Challenge) {

        val challengeData = hashMapOf(
            "title" to challenge.title,
            "description" to challenge.description,
            "points" to challenge.points,
            "completedBy" to challenge.completedBy,
        )

        firestore.collection("challenges").add(challengeData)
            .addOnSuccessListener { documentReference ->
                challenge.id = documentReference.id
                Log.d("Firestore", "Domanda aggiunta con ID: ${documentReference.id}")
                Log.d("challengeId", challenge.toString())
            }
            .addOnFailureListener { e ->
                println("Errore nell'aggiunta della challenge per  " +  challenge.id + ": $e")
            }
    }

    fun loadChallengesFromJson(context: Context) {
        firestore.collection("challenges").get().addOnSuccessListener { documents ->
            if (documents.isEmpty) {
                val inputStream = context.assets.open("challenges.json")
                val json = inputStream.bufferedReader().use { it.readText() }
                val challengesList: List<Challenge> = Gson().fromJson(json, object : TypeToken<List<Challenge>>() {}.type)

                challengesList.forEach { challenge ->
                    addChallenge(challenge)
                }
                Log.d("challengeList", challengesList.get(0).toString())
            }
        }
    }
}

data class Challenge(
    @get:PropertyName("id") @set:PropertyName("id") var id: String? = null,
    @get:PropertyName("title") @set:PropertyName("title") var title: String? = null,
    @get:PropertyName("description") @set:PropertyName("description") var description: String? = null,
    @get:PropertyName("points") @set:PropertyName("points") var points: Int = 0,
    @get:PropertyName("completedBy") @set:PropertyName("completedBy") var completedBy: List<String>? = null
) {
    constructor() : this(null, null, null, 0 ,null)
}