package com.example.smartlagoon.ui.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.smartlagoon.data.database.Photo_old
import com.example.smartlagoon.data.repository.UserRepository
import com.example.smartlagoon.utils.MarineClassifier
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

data class Photo(
    val photoId: String = "",
    val userId: String = "",
    val photoUrl: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

class PhotosDbViewModel(private val userRepository: UserRepository) : ViewModel() {

    val currentUser: LiveData<FirebaseUser?> = userRepository.currentUser
    private val _showDialog = MutableLiveData<Boolean>()
    val showDialog: LiveData<Boolean> = _showDialog

    private val _showDeleteDialog = MutableLiveData<Boolean>()
    val showDeleteDialog: LiveData<Boolean> = _showDeleteDialog

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val _category = MutableLiveData<MarineClassifier.Categories>()
    val category: LiveData<MarineClassifier.Categories> = _category
    // Funzione per mostrare o nascondere il dialogo
    fun setShowDialog(show: Boolean) {
        _showDialog.value = show
    }

    fun setShowDeleteDialog(show: Boolean) {
        _showDeleteDialog.value = show
    }

    fun setCategoryClassification(category: MarineClassifier.Categories) {
        _category.value = category

    }
    private val firestore = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val _photosLiveData = MutableLiveData<List<Photo>>()
    val photosLiveData: LiveData<List<Photo>> get() = _photosLiveData

    // Metodo per caricare una foto e salvarla nel database
    fun uploadPhoto(uri: Uri) {
        currentUser.value?.email?.let { Log.d("mailUploadFoto", it) }
        val userId = currentUser.value?.uid
        if (userId != null) {
            Log.d("upload", userId)
        }
        auth.currentUser?.email?.let { Log.d("upload", it) }
        val photoId = UUID.randomUUID().toString()
        val storageRef = storage.reference.child("photos/$photoId.jpg")
        Log.e("test","son qui")

        // Carica la foto su Firebase Storage
        storageRef.putFile(uri)
            .addOnSuccessListener {
                // Recupera l'URL della foto dal Firebase Storage
                storageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                    if (userId != null) {
                        savePhotoToFirestore(userId, photoId, downloadUri.toString())
                    }else {
                        println("userId null")
                    }
                }
                Log.e("PhotoDbViewModel", "Foto caricata")
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
                //fetchPhotosByUser(userId) // Aggiorna l'elenco delle foto dell'utente
                fetchAllPhotos()
            }
            .addOnFailureListener { exception ->
                Log.e("PhotoDbViewModel", "Errore durante il salvataggio della foto: ", exception)
            }
    }

    // Metodo per recuperare tutte le foto di un utente specifico
    fun fetchPhotosByUser(userId: String) {
        firestore.collection("photos")
            .whereEqualTo("userId", userId)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                val photos = result.documents.mapNotNull { it.toObject(Photo::class.java) }
                _photosLiveData.value = photos
                Log.e("PhotoDbViewModel", "Aggiornamento foto effettuato")
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

    fun deletePhoto(photoId: String) {
        // Ottieni una referenza al documento della foto in Firestore
        val photoDocRef = firestore.collection("photos").document(photoId)

        // Ottieni una referenza al file dell'immagine in Firebase Storage
        val photoStorageRef = storage.reference.child("photos/$photoId.jpg")

        firestore.runTransaction { transaction ->
            val photoDoc = transaction.get(photoDocRef)
            if (photoDoc.exists()) {
                transaction.delete(photoDocRef)
            } else {
                throw Exception("Photo document does not exist.")
            }
        }.addOnSuccessListener {
            // Dopo aver eliminato il documento, elimina il file dell'immagine
            photoStorageRef.delete()
                .addOnSuccessListener {
                    _message.value = "Foto eliminata correttamente."
                    _showDeleteDialog.value = true
                    Log.d("deletePhoto", "Photo and image deleted successfully.")
                }
                .addOnFailureListener { e ->
                    _message.value = "Errore eliminazione foto nello storage"
                    _showDeleteDialog.value = true
                    Log.e("deletePhoto", "Error deleting image from storage: ${e.message}")
                }
        }.addOnFailureListener { e ->
            _message.value = "Errore eliminazione foto nel database"
            _showDeleteDialog.value = true
            Log.e("deletePhoto", "Error deleting photo document: ${e.message}")
        }

    }


}