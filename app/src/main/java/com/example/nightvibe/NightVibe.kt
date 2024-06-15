package com.example.nightvibe

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.nightvibe.navigation.Router

@Composable
fun NightVibe(){
    Surface(modifier = Modifier.fillMaxSize()){
        Router()
    }
}