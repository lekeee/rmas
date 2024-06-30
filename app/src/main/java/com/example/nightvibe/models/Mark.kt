package com.example.nightvibe.models

import com.google.firebase.firestore.DocumentId

data class Mark (
    @DocumentId
    val id: String = "",
    val userId: String = "",
    val placeId: String = "",
    var mark: Int = 0
)