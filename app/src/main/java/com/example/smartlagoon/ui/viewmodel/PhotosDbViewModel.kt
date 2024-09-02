package com.example.smartlagoon.ui.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.smartlagoon.data.database.Photo_old
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

data class PhotosDbState(val photoOlds: List<Photo_old>)

data class Photo(
    val photoId: String = "",
    val userId: String = "",
    val photoUrl: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

class PhotosDbViewModel() : ViewModel() {

    private val _showDialog = MutableLiveData<Boolean>()
    val showDialog: LiveData<Boolean> = _showDialog

    // Funzione per mostrare o nascondere il dialogo
    fun setShowDialog(show: Boolean) {
        _showDialog.value = show
    }
    private val firestore = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser

    private val _photosLiveData = MutableLiveData<List<Photo>>()
    val photosLiveData: LiveData<List<Photo>> get() = _photosLiveData

    // Metodo per caricare una foto e salvarla nel database
    fun uploadPhoto(uri: Uri) {
        val userId = currentUser?.uid ?: return
        val photoId = UUID.randomUUID().toString()
        val storageRef = storage.reference.child("photos/$userId/$photoId.jpg")

        // Carica la foto su Firebase Storage
        storageRef.putFile(uri)
            .addOnSuccessListener {
                // Recupera l'URL della foto dal Firebase Storage
                storageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                    savePhotoToFirestore(userId, photoId, downloadUri.toString())
                }
            }
            .addOnFailureListener { exception ->
                Log.e("PhotoDbViewModel", "Errore durante il caricamento della foto: ", exception)
            }
    }

    // Salva i dettagli della foto nel database Firestore
    private fun savePhotoToFirestore(userId: String, photoId: String, photoUrl: String) {
        val photo = Photo(photoId, userId, photoUrl)
        firestore.collection("photos").document(photoId)
            .set(photo)
            .addOnSuccessListener {
                Log.d("PhotoDbViewModel", "Foto salvata con successo nel database")
                fetchPhotosByUser(userId) // Aggiorna l'elenco delle foto dell'utente
            }
            .addOnFailureListener { exception ->
                Log.e("PhotoDbViewModel", "Errore durante il salvataggio della foto: ", exception)
            }
    }

    // Metodo per recuperare tutte le foto di un utente specifico
    fun fetchPhotosByUser(userId: String) {
        firestore.collection("photos")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { result ->
                val photos = result.documents.mapNotNull { it.toObject(Photo::class.java) }
                _photosLiveData.value = photos
            }
            .addOnFailureListener { exception ->
                Log.e("PhotoDbViewModel", "Errore durante il recupero delle foto: ", exception)
            }
    }

    fun fetchAllPhotos() {
        firestore.collection("photos")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                val photos = result.documents.mapNotNull { it.toObject(Photo::class.java) }
                _photosLiveData.value = photos
            }
            .addOnFailureListener { exception ->
                Log.e("PhotoDbViewModel", "Errore durante il recupero delle foto: ", exception)
            }
    }

}