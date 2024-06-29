package com.example.nightvibe.models

import android.accounts.AuthenticatorDescription
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.GeoPoint

data class Place (
    @DocumentId
    val id: String = "",
    val userId: String = "",
    val name: String = "",
    val attendance: Int = 0,
    val description: String = "",
    val logo: String = "",
    val images: List<String> = emptyList(),
    val location: GeoPoint =  GeoPoint(0.0, 0.0)
)