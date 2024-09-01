package com.example.smartlagoon.ui.viewmodel

import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class UsersDbViewModel : ViewModel() {
    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    // LiveData per osservare i risultati del login e del signin
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

    private val _rankingLiveData = MutableLiveData<List<User?>>()
    val rankingLiveData: LiveData<List<User?>> = _rankingLiveData

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


    // Funzione per gestire il login
    fun login(username: String, password: String, sharedPreferences: SharedPreferences) {
        auth.signInWithEmailAndPassword(username, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _loginResult.value = true
                    _loginLog.value = "Login successful"

                    // Salva i dati dell'utente nelle SharedPreferences
                    val edit = sharedPreferences.edit()
                    edit.putBoolean("isUserLogged", true)
                    edit.putString("username", username)
                    edit.putString("password", password)
                    edit.apply()
                } else {
                    _loginResult.value = false
                    _loginLog.value = task.exception?.message ?: "Login failed"
                }
            }
    }

    // Funzione per gestire la registrazione
    fun register(
        email: String,
        password: String,
        firstName: String,
        lastName: String,
        username: String,
        points: Int = 0,
        profileImageUrl: String = ""
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    user?.let {
                        val userData = mapOf(
                            "name" to firstName,
                            "surname" to lastName,
                            "username" to username,
                            "email" to email,
                            "points" to points,
                            "profileImageUrl" to profileImageUrl
                        )
                        firestore.collection("users").document(it.uid).set(userData)
                            .addOnSuccessListener {
                                _signinResult.value = true
                                _signinLog.value = "User registered successfully."
                            }
                            .addOnFailureListener { e ->
                                _signinResult.value = false
                                _signinLog.value = "Error saving user data: ${e.message}"
                            }
                    }
                } else {
                    _signinResult.value = false
                    _signinLog.value = task.exception?.message ?: "Registration failed"
                }
            }
    }

    fun addPoints(challengePoints: Int) {
        val user = auth.currentUser
        user?.let {
            // Recupera il documento dell'utente da Firestore
            val userDocRef = firestore.collection("users").document(it.uid)

            firestore.runTransaction { transaction ->
                // Ottieni il documento dell'utente
                val userDoc = transaction.get(userDocRef)

                // Ottieni i punti attuali
                val currentPoints = userDoc.getLong("points") ?: 0

                // Calcola i nuovi punti
                val newPoints = currentPoints + challengePoints

                // Aggiorna il documento con i nuovi punti
                transaction.update(userDocRef, "points", newPoints)
                Log.d("addPoints2","punti aggiunti")
            }.addOnSuccessListener {
                Log.d("addPoints","punti aggiunti")
                _log.value = "Points added successfully."
                fetchUserProfile()
            }.addOnFailureListener { e ->
                _log.value = "Error updating points: ${e.message}"
            }
        } ?: run {
            _log.value = "No user is currently logged in."
        }
    }

    fun uploadProfileImage(userId: String, imageUri: Uri, onComplete: () -> Unit) {
        val storageRef: StorageReference = FirebaseStorage.getInstance().reference
        val userProfileImageRef = storageRef.child("profile_images/${userId}.jpg")

        userProfileImageRef.putFile(imageUri)
            .addOnSuccessListener {
                userProfileImageRef.downloadUrl.addOnSuccessListener { uri ->
                    updateProfileImageUrl(userId, uri.toString()) {
                        // Chiamata al callback dopo l'aggiornamento dell'URL
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
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val userId = currentUser.uid
            val userDocRef = firestore.collection("users").document(userId)

            val updatedData = mutableMapOf<String, Any>()

            // Aggiungi solo i valori non vuoti
            if (name.isNotBlank()) updatedData["name"] = name
            if (surname.isNotBlank()) updatedData["surname"] = surname
            if (username.isNotBlank()) updatedData["username"] = username

            // Solo se ci sono dati da aggiornare
            if (updatedData.isNotEmpty()) {
                userDocRef.update(updatedData)
                    .addOnSuccessListener {
                        Log.d("Firebase", "User profile updated successfully.")
                        fetchUserProfile()
                    }
                    .addOnFailureListener { e ->
                        Log.e("Firebase", "Error updating user profile", e)
                    }
            }
        }
    }

    // Funzione per recuperare i dati del profilo utente
    fun fetchUserProfileByUsername(username: String) {
        firestore.collection("users")
            .whereEqualTo("username", username)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (querySnapshot.isEmpty) {
                    _userLiveData.value = null
                    Log.d("fetch", "Nessun utente trovato con l'username: $username")
                } else {
                    // Supponiamo che ci sia solo un utente con un determinato username
                    val document = querySnapshot.documents.firstOrNull()
                    val usr = document?.toObject(User::class.java)
                    _userLiveData.postValue(usr)
                    Log.d("fetch", "Profilo utente aggiornato per l'username: $username")
                }
            }
            .addOnFailureListener { e ->
                Log.e("Firebase", "Error fetching profile: ${e.message}")
            }
    }

    // Funzione per recuperare i dati del profilo utente
    fun fetchUserProfile() {
        auth.currentUser?.let { user ->
            firestore.collection("users").document(user.uid)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val usr = document.toObject(User::class.java)
                        //_userLiveData.value = user
                        _userLiveData.postValue(usr)
                        Log.d("fetch", "update fatto")
                    } else {
                        _userLiveData.value = null
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("Firebase", "Error fetching profile: ${e.message}")
                }
        }
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

// Definizione della classe UserData (modifica secondo il tuo schema)
data class User(
    val name: String? = null,
    val surname: String? = null,
    val username: String? = null,
    val email: String? = null,
    val points: Int = 0,
    val profileImageUrl: String = ""
)
