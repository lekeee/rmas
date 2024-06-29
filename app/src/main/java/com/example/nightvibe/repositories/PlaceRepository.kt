package com.example.nightvibe.repositories

import android.net.Uri
import com.example.nightvibe.models.Place
import com.google.android.gms.maps.model.LatLng

interface PlaceRepository {
    suspend fun getAllPlaces(): Resource<List<Place>>
    suspend fun getUserPlaces(userId: String): Resource<List<Place>>
    suspend fun savePlace(
        name: String,
        attendance: Int,
        description: String,
        logo: Uri,
        images: List<Uri>,
        location: LatLng
    ): Resource<String>

}