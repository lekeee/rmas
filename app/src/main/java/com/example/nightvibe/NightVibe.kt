package com.example.nightvibe

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.nightvibe.navigation.Router
import com.example.nightvibe.viewmodels.AuthViewModel

@Composable
fun NightVibe(authViewModel: AuthViewModel){
    Surface(modifier = Modifier.fillMaxSize()){
        Router(authVM = authViewModel)
    }
}