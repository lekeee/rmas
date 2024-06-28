package com.example.nightvibe.screens

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.NavController
import com.example.nightvibe.R
import com.example.nightvibe.navigation.Routes
import com.example.nightvibe.screens.components.myPositionIndicator
import com.example.nightvibe.services.LocationService
import com.example.nightvibe.viewmodels.AuthViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import com.google.maps.android.ktx.model.cameraPosition


@Composable
fun IndexScreen(
    navController: NavController,
    viewModel: AuthViewModel,
    cameraPosition : CameraPositionState = rememberCameraPositionState(){
        position = CameraPosition.fromLatLngZoom(LatLng(42.96509503785226, 22.131214613702426), 17f)
    },
    isCameraSet: MutableState<Boolean> = remember {
        mutableStateOf(false)
}){
    val markers = remember { mutableStateListOf<LatLng>() }
    val properties = remember {
        mutableStateOf(MapProperties(mapType = MapType.TERRAIN))
    }
    val uiSettings = remember { mutableStateOf(MapUiSettings()) }

    val myLocation = remember {
        mutableStateOf<LatLng?>(null)
    }

    val receiver = remember {
        object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent?.action == LocationService.ACTION_LOCATION_UPDATE) {
                    val latitude =
                        intent.getDoubleExtra(LocationService.EXTRA_LOCATION_LATITUDE, 0.0)
                    val longitude =
                        intent.getDoubleExtra(LocationService.EXTRA_LOCATION_LONGITUDE, 0.0)
                    // Update the camera position
                    myLocation.value = LatLng(latitude, longitude)
                    Log.d("Nova lokacija", myLocation.toString())
                }
            }
        }
    }

    val context = LocalContext.current

    DisposableEffect(context) {
        LocalBroadcastManager.getInstance(context)
            .registerReceiver(receiver, IntentFilter(LocationService.ACTION_LOCATION_UPDATE))
        onDispose {
            LocalBroadcastManager.getInstance(context).unregisterReceiver(receiver)
        }
    }


    LaunchedEffect(myLocation.value) {
        myLocation.value?.let{
            if(!isCameraSet.value){
                Log.d("Nova lokacija gore", myLocation.toString())
                cameraPosition.position = CameraPosition.fromLatLngZoom(it, 17f)
                isCameraSet.value = true
            }
            markers.clear()
            markers.add(it)
        }
    }

    Column(modifier = Modifier
        .fillMaxWidth()
        .height(100.dp),) {

        Box(modifier = Modifier.fillMaxSize()){
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPosition,
                properties = properties.value,
                uiSettings = uiSettings.value
            ){
                markers.forEach { marker ->
                    val icon = myPositionIndicator(
                        context, R.drawable.my_location
                    )
                    Marker(
                        state = rememberMarkerState(position = marker),
                        title = "Moja Lokacija",
                        icon = icon,
                        snippet = "",
                    )
                }
            }
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
}