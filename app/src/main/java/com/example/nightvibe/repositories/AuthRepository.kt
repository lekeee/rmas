package com.example.nightvibe.repositories

import com.google.firebase.auth.FirebaseUser

interface AuthRepository {
    val user : FirebaseUser?

    suspend fun login(email: String, password: String)
}