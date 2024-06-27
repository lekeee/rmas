package com.example.nightvibe.repositories

import android.net.Uri
import com.example.nightvibe.User
import com.example.nightvibe.services.DatabaseService
import com.example.nightvibe.services.StorageService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

class AuthRepositoryImplementation : AuthRepository {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

    private val databaseService =  DatabaseService(firestore)
    private val storageService = StorageService(storage)

    override val user: FirebaseUser?
        get() = firebaseAuth.currentUser

    override suspend fun login(email: String, password: String): Resource<FirebaseUser> {
        return try{
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            Resource.Success(result.user!!)
        }catch (e: Exception){
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override fun logout() {
        firebaseAuth.signOut()
    }

    override suspend fun register(
        image: Uri,
        fullName: String,
        email: String,
        phone: String,
        password: String
    ): Resource<FirebaseUser> {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()

            if(result.user != null){
                val imageUrl = storageService.uploadUserImage(result.user!!.uid, image)
                val user = User(
                    fullName = fullName,
                    phone = phone,
                    image = imageUrl
                )
                databaseService.registerUser(result.user!!.uid, user)
            }
            Resource.Success(result.user!!)
        }catch(e: Exception){
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun getUser(): Resource<User> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllUsers(): Resource<List<User>> {
        TODO("Not yet implemented")
    }
}