package com.example.nightvibe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.nightvibe.factories.AuthViewModelFactory
import com.example.nightvibe.viewmodels.AuthViewModel

class MainActivity : ComponentActivity(){
    private val userViewModel: AuthViewModel by viewModels {
        AuthViewModelFactory()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{
            NightVibe(userViewModel)
        }
    }
}