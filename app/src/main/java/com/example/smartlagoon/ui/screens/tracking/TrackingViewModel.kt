package com.example.smartlagoon.ui.screens.tracking

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class TrackingState(

    val showLocationPermissionDenied: Boolean = false
)
interface TrackingActions {
    fun setShowLocationPermissionDenied(show: Boolean)
}

class TrackingViewModel : ViewModel() {

    private val _isTracking: MutableLiveData<Boolean?> = MutableLiveData<Boolean?>()
    val isTracking: LiveData<Boolean?> = _isTracking
    private val _duration = MutableLiveData<Long>()
    val duration: LiveData<Long> = _duration
    private val _distance = MutableLiveData<Int>()
    val distance: LiveData<Int> = _distance
    private val _steps = MutableLiveData<Int>()
    val steps: LiveData<Int> = _steps
    private val _state = MutableStateFlow(TrackingState())
    val state = _state.asStateFlow()


    val actions = object : TrackingActions {

        override fun setShowLocationPermissionDenied(show: Boolean) =
            _state.update { it.copy(showLocationPermissionDenied = show) }

    }
}