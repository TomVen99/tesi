package com.example.smartlagoon.ui.viewmodel

import android.content.SharedPreferences
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartlagoon.data.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.launch

class UsersDbViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    val currentUser: LiveData<FirebaseUser?> = userRepository.currentUser

    fun logout() {
        Log.e("Logout", "chiamoLogOut")
        viewModelScope.launch {
            userRepository.logout()
        }
    }

    override fun onCleared() {
        super.onCleared()
        userRepository.cleanUp()
    }

    private val _showModifyButton = MutableLiveData<Boolean?>()
    val showModifyButton: LiveData<Boolean?> = _showModifyButton

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _loginResult = MutableLiveData<Boolean?>()
    val loginResult: LiveData<Boolean?> = _loginResult

    private val _loginLog = MutableLiveData<String>()
    val loginLog: LiveData<String> = _loginLog

    private val _signinResult = MutableLiveData<Boolean?>()
    val signinResult: LiveData<Boolean?> = _signinResult

    private val _signinLog = MutableLiveData<String>()
    val signinLog: LiveData<String> = _signinLog

    private val _log = MutableLiveData<String>()
    val log: LiveData<String> = _log

    private val _userLiveData = MutableLiveData<User?>()
    val userLiveData: LiveData<User?> = _userLiveData

    private val _tmpUserLiveData = MutableLiveData<User?>()
    val tmpUserLiveData: LiveData<User?> = _tmpUserLiveData

    private val _rankingLiveData = MutableLiveData<List<User?>>()
    val rankingLiveData: LiveData<List<User?>> = _rankingLiveData

    fun setNullTmpUserLiveData(){
        _tmpUserLiveData.value = null
    }

    fun setShowModifyButton(value: Boolean) {
        _showModifyButton.value = value
    }

    fun setLoginResult() {
         _loginResult.value = false
     }
    fun getRanking() {
        firestore.collection("users")
            .orderBy("points", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                val ranking = result.documents.mapNotNull { it.toObject(User::class.java) }
                _rankingLiveData.value = ranking
            }
            .addOnFailureListener { exception ->
                Log.e("PhotoDbViewModel", "Errore durante il recupero delle foto: ", exception)
            }
    }

    fun getUser(userId: String, callback: (User?) -> Unit) {
        val userDocRef = FirebaseFirestore.getInstance().collection("users").document(userId)
        userDocRef.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val user = document.toObject(User::class.java)
                    callback(user)
                } else {
                    callback(null)
                }
            }
            .addOnFailureListener {
                callback(null)
            }
    }
    fun login(email: String, password: String, sharedPreferences: SharedPreferences) {
        viewModelScope.launch {
            userRepository.login(email, password, sharedPreferences) { success, message ->
                _loginResult.value = success
                _loginLog.value = message
                Log.d("login", message)
            }
        }
    }

    fun register(
        email: String,
        password: String,
        firstName: String,
        lastName: String,
        username: String,
        points: Int = 0,
        profileImageUrl: String = ""
    ) {
        userRepository.register(email, password, firstName, lastName, username, points, profileImageUrl) { success, message ->
            _signinResult.postValue(success)
            _signinLog.postValue(message)
        }
    }

    fun addPoints(challengePoints: Int) {
        userRepository.addPoints(challengePoints) { success, message ->
            _log.postValue(message)
            if (success) {
                fetchUserProfile()
            }
        }
    }
    fun uploadProfileImage(userId: String, imageUri: Uri, onComplete: () -> Unit) {
        val storageRef: StorageReference = FirebaseStorage.getInstance().reference
        val userProfileImageRef = storageRef.child("profile_images/${userId}.jpg")

        userProfileImageRef.putFile(imageUri)
            .addOnSuccessListener {
                userProfileImageRef.downloadUrl.addOnSuccessListener { uri ->
                    updateProfileImageUrl(userId, uri.toString()) {
                        onComplete()
                    }
                }.addOnFailureListener { e ->
                    Log.e("Firebase", "Error getting image URL", e)
                }
            }
            .addOnFailureListener { e ->
                Log.e("Firebase", "Error uploading image", e)
            }
    }

    private fun updateProfileImageUrl(userId: String, imageUrl: String, onComplete: () -> Unit) {
        val firestore = FirebaseFirestore.getInstance()
        val userDocRef = firestore.collection("users").document(userId)

        userDocRef.update("profileImageUrl", imageUrl)
            .addOnSuccessListener {
                Log.d("Firebase", "Profile image URL updated successfully.")
                onComplete()
            }
            .addOnFailureListener { e ->
                Log.e("Firebase", "Error updating profile image URL", e)
            }
    }


    fun updateUserProfile(name: String, surname: String, username: String) {
        userRepository.updateUserProfile(name, surname, username) { success ->
            if (success) {
                fetchUserProfile()
            } else {
                Log.e("UserViewModel", "Failed to update user profile")
            }
        }
    }

    fun fetchUserProfileByUsername(username: String) {
        firestore.collection("users")
            .whereEqualTo("username", username)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (querySnapshot.isEmpty) {
                    _tmpUserLiveData.value = null
                    Log.d("fetch", "Nessun utente trovato con l'username: $username")
                } else {
                    val document = querySnapshot.documents.firstOrNull()
                    val usr = document?.toObject(User::class.java)
                    _tmpUserLiveData.value = usr
                    Log.d("fetch", "Profilo utente aggiornato per l'username: $username")
                }
            }
            .addOnFailureListener { e ->
                Log.e("Firebase", "Error fetching profile: ${e.message}")
            }
    }

    fun fetchUserProfile() {
        _isLoading.value = true
        viewModelScope.launch {
            userRepository.fetchUserProfile { user ->
                _userLiveData.value = user
                _isLoading.value = false
            }
        }
        _tmpUserLiveData.value = null
    }

    private fun Map<String, Any?>.toUser(): User {
        return User(
            name = this["name"] as? String,
            surname = this["surname"] as? String,
            username = this["username"] as? String,
            email = this["email"] as? String,
            points = (this["points"] as? Long)?.toInt() ?: 0,
            profileImageUrl = this["profileImageUrl"] as? String ?: ""
        )
    }
}
data class User(
    val name: String? = null,
    val surname: String? = null,
    val username: String? = null,
    val email: String? = null,
    val points: Int = 0,
    val profileImageUrl: String = ""
)
