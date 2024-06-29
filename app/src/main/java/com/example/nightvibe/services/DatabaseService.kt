package com.example.nightvibe.services

import com.example.nightvibe.models.Place
import com.example.nightvibe.models.User
import com.example.nightvibe.repositories.Resource
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class DatabaseService(
    private val firestore: FirebaseFirestore
) {
    suspend fun registerUser(
        userId: String,
        user: User
    ): Resource<String>{
        return try{
            firestore.collection("users").document(userId).set(user).await()
            Resource.Success("Uspešno registrovan korisnik")
        }catch (e: Exception){
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    suspend fun getUser(
        uid: String
    ):Resource<String>{
        return try {
            val userReference = firestore.collection("users").document(uid)
            val userSnapshot = userReference.get().await()

            if(userSnapshot.exists()){
                val user = userSnapshot.toObject(User::class.java)
                if(user != null){
                    Resource.Success(user)
                } else {
                    Resource.Failure(Exception("Korisnik ne postoji"))
                }
            } else {
                Resource.Failure(Exception("Korisnikov dokument ne postoji"))
            }
            Resource.Success("Uspešno")
        }catch (e: Exception){
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    suspend fun savePlace(place: Place): Resource<String>{
        return try{
            firestore.collection("places").add(place).await()
            Resource.Success("Uspešno dodato mesto")
        }catch(e: Exception){
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    suspend fun addScore(
        userId: String,
        value: Int
    ): Resource<String>{
        return try{
            val userReference = firestore.collection("users").document(userId)
            val userSnapshot = userReference.get().await()
            if(userSnapshot.exists()){
                val user = userSnapshot.toObject(User::class.java)
                if(user != null){
                    val newScore = user.score + value;
                    userReference.update("score", newScore).await()
                    Resource.Success("Uspesno povecan score korisnika")
                }else{
                    Resource.Failure(Exception("Korisnik ne postoji"))
                }
            }
            else{
                Resource.Failure(Exception("Korisnikov dokument ne postoji"))
            }
        }catch (e: Exception){
            e.printStackTrace()
            Resource.Failure(e)
        }
    }
}