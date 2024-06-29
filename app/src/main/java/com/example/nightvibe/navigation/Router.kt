package com.example.nightvibe.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.nightvibe.screens.AddPlaceScreen
import com.example.nightvibe.screens.IndexScreen
import com.example.nightvibe.screens.LoginScreen
import com.example.nightvibe.screens.RegisterScreen
import com.example.nightvibe.viewmodels.AuthViewModel
import com.example.nightvibe.viewmodels.PlaceViewModel
import com.google.android.gms.maps.model.LatLng


@Composable
fun Router(authVM : AuthViewModel, placeVM : PlaceViewModel){
    val navController = rememberNavController();
    NavHost(navController = navController, startDestination = Routes.loginScreen){
        composable(Routes.loginScreen){
            LoginScreen(navController = navController, viewModel = authVM)
        }
        composable(Routes.registerScreen){
            RegisterScreen(navController = navController, viewModel = authVM)
        }
        composable(Routes.indexScreen){
            IndexScreen(navController = navController, viewModel = authVM)
        }
        composable(
            Routes.addPlaceScreen+"/{latitude}/{longitude}",
            arguments = listOf(
                navArgument("latitude") { type = NavType.FloatType },
                navArgument("longitude") { type = NavType.FloatType }
            )
        ){ backStackEntry ->
            val latitude = backStackEntry.arguments?.getFloat("latitude")
            val longitude = backStackEntry.arguments?.getFloat("longitude")

            val location = remember {
                mutableStateOf(LatLng(latitude!!.toDouble(), longitude!!.toDouble()))
            }
            AddPlaceScreen(placeViewModel = placeVM, location = location, navController)
        }
    }
}
