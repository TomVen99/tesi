package com.example.smartlagoon.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartlagoon.data.repositories.ChallengeRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.PropertyName
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class ChallengesDbState(val challenges: List<Challenge>)

class ChallengesDbViewModel(
    private val repository: ChallengeRepository
) : ViewModel() {
    /*val state = repository.challenges.map { ChallengesDbState(challenges = it) }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = ChallengesDbState(emptyList())
    )*/

    private val _userChallengesNumber = MutableLiveData<Int>()
    val userChallengesNumber: LiveData<Int> = _userChallengesNumber

    private val _userUncompletedChallenges = MutableLiveData<List<Challenge>>()
    var userUncompleteChallenges: LiveData<List<Challenge>> = _userUncompletedChallenges

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val currentUser = auth.currentUser
    private val userId = currentUser?.uid
    fun getUnconpletedChallengeByUser() {
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

    fun createChallangeTest() = viewModelScope.launch{
      //  repository.generateChallengeTest()
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