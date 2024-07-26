package com.example.smartlagoon.ui.screens.addtrack

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class AddTrackState(
    var title: String = "",
    var description: String = "",
    var city: String = "",
    var startLat: Double = 0.0,
    var startLng: Double = 0.0,
    var duration: Long = 0,
    var trackPositions: List<LatLng> = listOf(),
    var imageUri: Uri = Uri.EMPTY,

    val showLocationPermissionDeniedAlert: Boolean = false,
    val showLocationPermissionPermanentlyDeniedSnackbar: Boolean = false,
) {
}

interface AddTrackActions {
    fun setTitle(title: String)
    fun setDescription(description: String)
    fun setCity(city: String)

    fun setStartLat(startLat: Double)
    fun setStartLng(startLng: Double)
    fun setDuration(duration: Long)
    fun setTrackPositions(trackPositions: List<LatLng>)
    fun setImageUri(imageUri: Uri)
    fun setShowLocationPermissionDeniedAlert(show: Boolean)
    fun setShowLocationPermissionPermanentlyDeniedSnackbar(show: Boolean)
}

class AddTrackViewModel : ViewModel() {
    private val _state = MutableStateFlow(AddTrackState())
    val state = _state.asStateFlow()

    val actions = object : AddTrackActions {
        override fun setTitle(title: String) =
            _state.update { it.copy(title = title) }

        override fun setDescription(description: String) =
            _state.update { it.copy(description = description) }

        override fun setCity(city: String) {
            _state.update { it.copy(city = city) }
        }

        override fun setStartLat(startLat: Double) {
            _state.update { it.copy(startLat = startLat) }
        }

        override fun setStartLng(startLng: Double) {
            _state.update { it.copy(startLng = startLng) }
        }

        override fun setDuration(duration: Long) {
            _state.update { it.copy(duration = duration) }
        }

        override fun setTrackPositions(trackPositions: List<LatLng>) {
            _state.update { it.copy(trackPositions = trackPositions) }
        }

        override fun setImageUri(imageUri: Uri) =
            _state.update { it.copy(imageUri = imageUri) }

        override fun setShowLocationPermissionDeniedAlert(show: Boolean) =
            _state.update { it.copy(showLocationPermissionDeniedAlert = show) }

        override fun setShowLocationPermissionPermanentlyDeniedSnackbar(show: Boolean) =
            _state.update { it.copy(showLocationPermissionPermanentlyDeniedSnackbar = show) }

    }
}