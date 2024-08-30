package com.example.smartlagoon.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartlagoon.data.database.User_old
import com.example.smartlagoon.data.repositories.UsersRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.security.MessageDigest
import java.security.SecureRandom

data class UsersState(val userOlds: List<User_old>)

class UsersViewModel(
    private val repository: UsersRepository
) : ViewModel() {
    private val _signinResult = MutableLiveData<Boolean?>()
    val signinResult: LiveData<Boolean?> = _signinResult
    private val _signinLog = MutableLiveData<String?>()
    val signinLog: LiveData<String?> = _signinLog

    private val _loginResult = MutableLiveData<Boolean?>()
    val loginResult: LiveData<Boolean?> = _loginResult
    private val _loginLog = MutableLiveData<String?>()
    val loginLog: LiveData<String?> = _loginLog

    private val _userPoints = MutableLiveData<Int>()
    val userPoints: LiveData<Int> = _userPoints

    /*val state = repository.users.map { UsersState(userOlds = it) }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = UsersState(emptyList())
    )

    val rankingState = repository.usersRanking.map { UsersState(userOlds = it) }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = UsersState(emptyList())
    )

    fun getUserPoints(username: String) = viewModelScope.launch {
        val userPoints = withContext(Dispatchers.IO) {
            repository.getUserPoints(username)
        }
        _userPoints.value = userPoints
    }

    suspend fun getUser(username: String): User_old? {
        return repository.getUser(username).firstOrNull()
    }



    fun addPoints(username: String, points: Int) {
        Log.d("points", "son qui")
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.addPoints(username, points)
            }
            // Dopo l'aggiornamento, otteniamo il nuovo valore dei punti
            val newPoints = withContext(Dispatchers.IO) {
                repository.getUserPoints(username)
            }
            _userPoints.value = newPoints
        }
    }

    fun addUser(userOld: User_old) {
        Log.d("TAG", "addUser")
        viewModelScope.launch {
            val userMatch = repository.getUser(userOld.username).firstOrNull()
            if (userMatch == null) {
                repository.upsert(userOld)
                _signinResult.value = true
            } else {
                _signinResult.value = false
                _signinLog.value = "errore: username già esistente"
            }

        }
    }

    fun addUserWithoutControl(userOld: User_old) {
        viewModelScope.launch {
            repository.upsert(userOld)
        }
    }

    fun login(username: String, password: String/*user:User*/) {
        viewModelScope.launch {
            val userMatch = repository.getUser(/*user.*/username).firstOrNull()
            if (userMatch != null ) {
                _loginResult.value = userMatch.password == hashPassword(/*user.*/password, userMatch.salt)
                _loginLog.value = if (_loginResult.value!!) "" else "errore: Password sbagliata"
            } else {
                _loginResult.value = false
                _loginLog.value = "errore: Username non esiste"
            }
        }

    }
    fun resetValues() {
        _signinResult.value = null
        _loginResult.value = null
    }

    fun deleteUser(userOld: User_old) = viewModelScope.launch {
        repository.delete(userOld)
    }

    fun hashPassword(password: String, salt: ByteArray): String {
        val bytes = password.toByteArray() + salt
        val digest = MessageDigest.getInstance("SHA-256").digest(bytes)
        return digest.fold("") { str, it -> str + "%02x".format(it) }
    }
    fun generateSalt(length: Int = 16): ByteArray {
        val salt = ByteArray(length)
        val random = SecureRandom()
        random.nextBytes(salt)
        return salt
    }

    fun updateProfileImg(username: String, profileImg: String) = viewModelScope.launch {
        repository.updateProfileImg(username, profileImg)
    }
*/
}

