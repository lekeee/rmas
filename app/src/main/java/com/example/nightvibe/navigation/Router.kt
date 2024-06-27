package com.example.nightvibe.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.nightvibe.screens.IndexScreen
import com.example.nightvibe.screens.LoginScreen
import com.example.nightvibe.screens.RegisterScreen

@Composable
fun Router(){
    val navController = rememberNavController();
    NavHost(navController = navController, startDestination = Routes.loginScreen){
        composable(Routes.loginScreen){
            LoginScreen(navController = navController)
        }
        composable(Routes.registerScreen){
            RegisterScreen(navController = navController)
        }
        composable(Routes.indexScreen){
            IndexScreen(navController = navController)
        }
    }
}
