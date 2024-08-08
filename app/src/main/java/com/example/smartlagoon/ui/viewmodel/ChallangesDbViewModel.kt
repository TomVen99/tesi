package com.example.smartlagoon.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.smartlagoon.data.database.Challenge
import com.example.smartlagoon.data.database.Photo
import com.example.smartlagoon.data.repositories.ChallengeRepository
import com.example.smartlagoon.data.repositories.PhotosRepository
import com.example.smartlagoon.data.repositories.TracksRepository
import com.example.smartlagoon.utils.NotificationWorker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

data class ChallengesDbState(val challenges: List<Challenge>)

class ChallengesDbViewModel(
    private val repository: ChallengeRepository
) : ViewModel() {
    val state = repository.challenges.map { ChallengesDbState(challenges = it) }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = ChallengesDbState(emptyList())
    )

    private val _userChallengesNumber = MutableLiveData<Int>()
    val userChallengesNumber: LiveData<Int> = _userChallengesNumber

    private val _userUncompletedChallenges = MutableLiveData<List<Challenge>>()
    var userUncompleteChallenges: LiveData<List<Challenge>> = _userUncompletedChallenges

    fun getAllChallenges() = viewModelScope.launch {
        repository.getAllChallenges()
    }

    fun getUncompletedChallengesForUser(username: String) = viewModelScope.launch {
        val userUncompletedChallenges = repository.getUncompletedChallengesForUser(username)
        _userUncompletedChallenges.value = userUncompletedChallenges
    }

    fun insertTest() = viewModelScope.launch{
        repository.generateChallengeTest()
        repository.generateDoneChallenge()
    }

}