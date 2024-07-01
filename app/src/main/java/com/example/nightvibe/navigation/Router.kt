package com.example.nightvibe.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.nightvibe.models.Place
import com.example.nightvibe.repositories.Resource
import com.example.nightvibe.screens.AddPlaceScreen
import com.example.nightvibe.screens.IndexScreen
import com.example.nightvibe.screens.LoginScreen
import com.example.nightvibe.screens.PlaceScreen
import com.example.nightvibe.screens.RegisterScreen
import com.example.nightvibe.viewmodels.AuthViewModel
import com.example.nightvibe.viewmodels.PlaceViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.google.maps.android.compose.rememberCameraPositionState


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
            val placesData = placeVM.places.collectAsState()
            val placesPins = remember {
                mutableListOf<Place>()
            }
            placesData.value.let {
                when(it){
                    is Resource.Success -> {
                        placesPins.clear()
                        placesPins.addAll(it.result)
                    }
                    is Resource.loading -> {

                    }
                    is Resource.Failure -> {
                        Log.e("Podaci", it.toString())
                    }
                    null -> {}
                }
            }
            IndexScreen(navController = navController, viewModel = authVM, placeViewModel = placeVM, placesMarkers = placesPins)
        }
        composable(
            route = Routes.indexScreen + "/{camera}/{latitude}/{longitude}/{places}",
            arguments = listOf(
                navArgument("camera") { type = NavType.BoolType },
                navArgument("latitude") { type = NavType.FloatType },
                navArgument("longitude") { type = NavType.FloatType },
                navArgument("places") { type = NavType.StringType }
            )
        ){
            backStackEntry ->
            val camera = backStackEntry.arguments?.getBoolean("camera")
            val latitude = backStackEntry.arguments?.getFloat("latitude")
            val longitude = backStackEntry.arguments?.getFloat("longitude")
            val placesJson = backStackEntry.arguments?.getString("places")
            val places = Gson().fromJson(placesJson, Array<Place>::class.java).toList()

            IndexScreen(
                navController = navController,
                viewModel = authVM,
                placeViewModel = placeVM,
                placesMarkers = places.toMutableList(),
                isCameraSet = remember {
                    mutableStateOf(camera!!)
                },
                cameraPosition = rememberCameraPositionState{
                    position = CameraPosition.fromLatLngZoom(LatLng(latitude!!.toDouble(), longitude!!.toDouble()), 17f)
                }
            )
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
        composable(
            route = Routes.placeScreen + "/{place}/{places}",
            arguments = listOf(
                navArgument("place"){ type = NavType.StringType },
                navArgument("places"){ type = NavType.StringType },
            )
        ){
            backStackEntry ->
            val placesJson = backStackEntry.arguments?.getString("places")
            val places = Gson().fromJson(placesJson, Array<Place>::class.java).toList()
            val placeJson = backStackEntry.arguments?.getString("place")
            val place = Gson().fromJson(placeJson, Place::class.java)

            placeVM.getPlaceMarks(place.id)

            PlaceScreen(
                navController = navController,
                placeViewModel = placeVM,
                authViewModel = authVM,
                place = place,
                places = places.toMutableList()
            )
        }
    }
}
