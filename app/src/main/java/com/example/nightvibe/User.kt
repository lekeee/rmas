package com.example.nightvibe

import com.google.firebase.firestore.DocumentId

data class User (
    @DocumentId var id: String = "",
    val image : String  = "",
    val fullName: String = "",
    val phone: String = "",
    val score: Int = 0
)

