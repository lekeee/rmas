package com.example.nightvibe.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.nightvibe.navigation.Routes
import com.example.nightvibe.viewmodels.AuthViewModel

@Composable
fun IndexScreen(navController: NavController, viewModel: AuthViewModel){
    Column(modifier = Modifier.fillMaxWidth().height(100.dp),) {
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
            viewModel.logout()
            navController.navigate(route = Routes.loginScreen)
            }
        ) {
            Text(text = "Logout")
        }
    }
}