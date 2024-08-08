package com.example.smartlagoon.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartlagoon.data.database.Challenge
import com.example.smartlagoon.data.repositories.ChallengeRepository
import com.example.smartlagoon.data.repositories.UserChallengeRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class UserChallengeState(val challenges: List<Challenge>)

class UserChallengeViewModel(
    private val repository: UserChallengeRepository
) : ViewModel() {
    /*val state = repository.challenges.map { UserChallengeState(challenges = it) }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = UserChallengeState(emptyList())
    )*/

    private val _userChallengesNumber = MutableLiveData<Int>()
    val userChallengesNumber: LiveData<Int> = _userChallengesNumber

    fun insertTest() = viewModelScope.launch{
        repository.insertChallengeTest()
    }

}