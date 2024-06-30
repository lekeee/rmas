package com.example.nightvibe

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import com.example.nightvibe.factories.AuthViewModelFactory
import com.example.nightvibe.factories.PlaceViewModelFactory
import com.example.nightvibe.viewmodels.AuthViewModel
import com.example.nightvibe.viewmodels.PlaceViewModel


class MainActivity : ComponentActivity(){
    private val userViewModel: AuthViewModel by viewModels {
        AuthViewModelFactory()
    }
    private val placeViewModel: PlaceViewModel by viewModels{
        PlaceViewModelFactory()
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{
            NightVibe(userViewModel, placeViewModel)
        }
    }
}