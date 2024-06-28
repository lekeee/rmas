package com.example.nightvibe.repositories

import android.location.Location
import kotlinx.coroutines.flow.Flow

interface LocationRepository {
    fun LocationUpdates(interval: Long): Flow<Location>
    class LocationException(message: String): Exception()
}