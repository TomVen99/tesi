package com.example.smartlagoon.ui.screens.addtrackdetails

import android.net.Uri
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class AddTrackDetailsState(
    val title: String = "",
    val description: String = "",
    val imageUri: Uri = Uri.EMPTY,

) {
    val canSubmit get() = title.isNotBlank() &&  description.isNotBlank()
}

interface AddTrackDetailsActions {
    fun setTitle(title: String)
    fun setDescription(description: String)
    fun setImageUri(imageUri: Uri)
}

class AddTrackDetailsViewModel : ViewModel() {
    private val _state = MutableStateFlow(AddTrackDetailsState())
    val state = _state.asStateFlow()

    val actions = object : AddTrackDetailsActions {
        override fun setTitle(title: String) =
            _state.update { it.copy(title = title) }

        override fun setDescription(description: String) =
            _state.update { it.copy(description = description) }

        override fun setImageUri(imageUri: Uri) =
            _state.update { it.copy(imageUri = imageUri) }
    }
}