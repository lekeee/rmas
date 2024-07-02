package com.example.nightvibe.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.nightvibe.R
import com.example.nightvibe.models.Place
import com.example.nightvibe.navigation.Routes
import com.example.nightvibe.repositories.Resource
import com.example.nightvibe.screens.components.PlaceRow
import com.example.nightvibe.ui.theme.lightGray
import com.example.nightvibe.viewmodels.PlaceViewModel
import com.google.gson.Gson
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun PlacesScreen(
    places: List<Place>?,
    navController: NavController,
    placeViewModel: PlaceViewModel
) {
    val placesList = remember {
        mutableListOf<Place>()
    }
    if(places.isNullOrEmpty()){
        val placesResource = placeViewModel.places.collectAsState()
        placesResource.value.let{
            when(it){
                is Resource.Success -> {
                    Log.d("Podaci", it.toString())
                    placesList.clear()
                    placesList.addAll(it.result)
                }
                is Resource.loading -> {

                }
                is Resource.Failure -> {
                    Log.e("Podaci", it.toString())
                }
                null -> {}
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .padding(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(20.dp))
                    Image(painter = painterResource(id = R.drawable.nightout), contentDescription = "", modifier = Modifier.size(100.dp))
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = "Sve opcije za izlazak večeras",
                        modifier = Modifier.fillMaxWidth(),
                        style= TextStyle(
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            if(places.isNullOrEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .height(300.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.notfound),
                            contentDescription = "",
                            modifier = Modifier.size(150.dp)
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(text = "Trenutno nemamo ni jedno mesto")
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    item {
                        places.forEach{ place ->
                            PlaceRow(
                                place = place,
                                placeScreen = {
                                    val placeJson = Gson().toJson(place)
                                    val encoded = URLEncoder.encode(placeJson, StandardCharsets.UTF_8.toString())
                                    navController.navigate(Routes.placeScreen + "/$encoded")
                                },
                                placeOnMap = {
                                    val isCameraSet = true
                                    val latitude = place.location.latitude
                                    val longitude = place.location.longitude

                                    val placesJson = Gson().toJson(places)
                                    val encodedPlaces = URLEncoder.encode(placesJson, StandardCharsets.UTF_8.toString())
                                    navController.navigate(Routes.indexScreen + "/$isCameraSet/$latitude/$longitude/$encodedPlaces")
                                }
                            )
                            Spacer(modifier = Modifier.height(5.dp))
                        }
                    }



                }
            }
        }
    }
}
