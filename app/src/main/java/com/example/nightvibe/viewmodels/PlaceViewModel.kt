package com.example.nightvibe.viewmodels

import android.net.Uri
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.nightvibe.models.Place
import com.example.nightvibe.repositories.PlaceRepositoryImplementation
import com.example.nightvibe.repositories.Resource
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PlaceViewModel : ViewModel() {
    val repository = PlaceRepositoryImplementation()

    private val _placeFlow = MutableStateFlow<Resource<String>?>(null)
    val placeFlow: StateFlow<Resource<String>?> = _placeFlow

    private val _newScore = MutableStateFlow<Resource<String>?>(null)
    val newScore: StateFlow<Resource<String>?> = _newScore

    private val _places = MutableStateFlow<Resource<List<Place>>>(Resource.Success(emptyList()))
    val places: StateFlow<Resource<List<Place>>> get() = _places

    private val _userPlaces = MutableStateFlow<Resource<List<Place>>>(Resource.Success(emptyList()))
    val userPlaces: StateFlow<Resource<List<Place>>> get() = _userPlaces

    fun getAllPlaces() = viewModelScope.launch {
        _places.value = repository.getAllPlaces()
    }

    fun getUserPlaces(userId: String) = viewModelScope.launch {
        _userPlaces.value = repository.getUserPlaces(userId)
    }

    init{
        getAllPlaces()
    }

    fun savePlace(
        name: String,
        attendance: Int,
        description: String,
        logo: Uri,
        images: List<Uri>,
        location: MutableState<LatLng>?
    ) = viewModelScope.launch {
        _placeFlow.value = Resource.loading
        repository.savePlace(
            name = name,
            attendance = attendance,
            description = description,
            logo = logo,
            images = images,
            location = location!!.value
        )
        _placeFlow.value = Resource.Success("Uspesno dodato mesto")
    }
}

