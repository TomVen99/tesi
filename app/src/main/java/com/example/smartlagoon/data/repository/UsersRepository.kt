package com.example.smartlagoon.data.repository

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.smartlagoon.ui.viewmodel.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class UserRepository {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val _currentUser = MutableLiveData<FirebaseUser?>()
    val currentUser: LiveData<FirebaseUser?> = _currentUser

    private val authStateListener = FirebaseAuth.AuthStateListener { auth ->
        _currentUser.value = auth.currentUser
        auth.currentUser?.email?.let { Log.e("TEST", it) }
    }

    init {
        Log.e("TEST", "UserRepository initialized")
        firebaseAuth.addAuthStateListener(authStateListener)
    }

    fun register(
        email: String,
        password: String,
        firstName: String,
        lastName: String,
        username: String,
        points: Int = 0,
        profileImageUrl: String = "",
        onResult: (Boolean, String) -> Unit
    ) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = firebaseAuth.currentUser
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
                                onResult(true, "User registered successfully.")
                            }
                            .addOnFailureListener { e ->
                                onResult(false, "Error saving user data: ${e.message}")
                            }
                    }
                } else {
                    onResult(false, task.exception?.message ?: "Registration failed")
                }
            }
    }

    fun addPoints(
        challengePoints: Int,
        onResult: (Boolean, String) -> Unit
    ) {
        currentUser.let {
            val userDocRef = it.value?.uid?.let { it1 -> firestore.collection("users").document(it1) }

            firestore.runTransaction { transaction ->
                val userDoc = userDocRef?.let { it1 -> transaction.get(it1) }
                val currentPoints = userDoc?.getLong("points") ?: 0
                val newPoints = currentPoints + challengePoints
                if (userDocRef != null) {
                    transaction.update(userDocRef, "points", newPoints)
                }
                Log.d("addPoints2", "punti aggiunti")
            }.addOnSuccessListener {
                onResult(true, "Points added successfully.")
            }.addOnFailureListener { e ->
                onResult(false, "Error updating points: ${e.message}")
            }
        } ?: onResult(false, "No user is currently logged in.")
    }


    fun login(
        email: String,
        password: String,
        sharedPreferences: SharedPreferences,
        callback: (Boolean, String) -> Unit
    ) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val edit = sharedPreferences.edit()
                    edit.putBoolean("isUserLogged", true)
                    edit.putString("mail", email)
                    edit.putString("password", password)
                    edit.apply()

                    callback(true, "Login successful")
                    currentUser.value?.email?.let { Log.d("LoginnewUser", it) }
                } else {
                    callback(false, "Login failed: ${task.exception?.message}")
                }
            }
        _currentUser.value = firebaseAuth.currentUser
        currentUser.value?.email?.let { Log.d("LoginnewUser", it) }
    }
    fun fetchUserProfile(onResult: (User?) -> Unit) {
        val user = firebaseAuth.currentUser
        Log.d("UserRepository", "uis ${user?.uid}")
        user?.let {
            firestore.collection("users").document(user.uid)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val usr = document.toObject(User::class.java)
                        onResult(usr)
                        Log.e("UserRepository", "Update fatto")
                    } else {
                        onResult(null)
                        Log.e("UserRepository", "Documento non esiste")
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("UserRepository", "Error fetching profile: ${e.message}")
                    onResult(null)
                }
        } ?: onResult(null)
    }

    fun updateUserProfile(name: String, surname: String, username: String, onResult: (Boolean) -> Unit) {
        val userId = currentUser.value?.uid
        val userDocRef = userId?.let { firestore.collection("users").document(it) }

        val updatedData = mutableMapOf<String, Any>()
        if (name.isNotBlank()) updatedData["name"] = name
        if (surname.isNotBlank()) updatedData["surname"] = surname
        if (username.isNotBlank()) updatedData["username"] = username

        if (updatedData.isNotEmpty()) {
            userDocRef?.update(updatedData)?.addOnSuccessListener {
                Log.d("UserRepository", "User profile updated successfully.")
                onResult(true)
            }?.addOnFailureListener { e ->
                Log.e("UserRepository", "Error updating user profile", e)
                onResult(false)
            }
        } else {
            onResult(true)
        }
    }

    fun logout() {
        firebaseAuth.signOut()
        Log.e("Logout", currentUser.value.toString())
    }

    fun cleanUp() {
        firebaseAuth.removeAuthStateListener(authStateListener)
    }
}

