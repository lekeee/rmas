package com.example.nightvibe.repositories

import com.example.nightvibe.models.Mark
import com.example.nightvibe.models.Place

interface MarkRepository {
    suspend fun getMarks(
        placeId: String
    ) : Resource<List<Mark>>
    suspend fun getUserMarks() : Resource<List<Mark>>
    suspend fun addMark(
        placeId: String,
        mark: Int,
        place: Place
    ) : Resource<String>

    suspend fun updateMark(
        markId: String,
        mark: Int
    ) : Resource<String>
}