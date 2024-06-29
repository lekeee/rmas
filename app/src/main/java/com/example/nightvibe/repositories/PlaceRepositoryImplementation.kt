package com.example.nightvibe.repositories

import android.net.Uri
import com.example.nightvibe.models.Place
import com.example.nightvibe.services.DatabaseService
import com.example.nightvibe.services.StorageService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.storage.FirebaseStorage
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.tasks.await

class PlaceRepositoryImplementation : PlaceRepository{
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private val databaseService =  DatabaseService(firestore)
    private val storageService = StorageService(storage)

    override suspend fun getAllPlaces(): Resource<List<Place>> {
        return try{
            val snapshot = firestore.collection("places").get().await()
            val places = snapshot.toObjects(Place::class.java)
            Resource.Success(places)
        }catch (e: Exception){
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun getUserPlaces(userId: String): Resource<List<Place>> {
        return try {
            val snapshot = firestore.collection("places")
                .whereEqualTo("userId", userId)
                .get()
                .await()
            val places = snapshot.toObjects(Place::class.java)
            Resource.Success(places)
        }catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun savePlace(
        name: String,
        attendance: Int,
        description: String,
        logo: Uri,
        images: List<Uri>,
        location: LatLng
    ): Resource<String> {
        return try{
            val user = firebaseAuth.currentUser
            if(user != null){
                val logoUrl = storageService.uploadPlaceLogo(logo)
                val imageUrls = storageService.uploadPlaceImages(images)
                val geoLocation = GeoPoint(
                    location.latitude,
                    location.longitude
                )
                val place = Place(
                    userId = user.uid,
                    name = name,
                    attendance = attendance,
                    description = description,
                    logo = logoUrl,
                    images = imageUrls,
                    location = geoLocation
                )
                databaseService.savePlace(place)
                databaseService.addScore(user.uid, 10)
            }
            Resource.Success("Uspesno dodato mesto")
        }catch (e: Exception){
            e.printStackTrace()
            Resource.Failure(e)
        }
    }
}