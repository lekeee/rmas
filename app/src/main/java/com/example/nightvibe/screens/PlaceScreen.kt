package com.example.nightvibe.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.nightvibe.models.Mark
import com.example.nightvibe.models.Place
import com.example.nightvibe.navigation.Routes
import com.example.nightvibe.repositories.Resource
import com.example.nightvibe.screens.components.AttendanceView
import com.example.nightvibe.screens.components.BackButton
import com.example.nightvibe.screens.components.Heading1Text
import com.example.nightvibe.screens.components.LocationView
import com.example.nightvibe.screens.components.MarkButton
import com.example.nightvibe.screens.components.PlaceImagesView
import com.example.nightvibe.screens.components.PlaceLogoImage
import com.example.nightvibe.viewmodels.AuthViewModel
import com.example.nightvibe.viewmodels.PlaceViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import java.math.RoundingMode
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun PlaceScreen(
    navController: NavController,
    placeViewModel: PlaceViewModel,
    authViewModel: AuthViewModel,
    place: Place,
    places: MutableList<Place>?
) {
    val marks = remember {
        mutableListOf<Mark>()
    }

    val myMark = remember {
        mutableStateOf("")
    }

    val showMarkScreen = remember {
        mutableStateOf(false)
    }

    val isLoading = remember {
        mutableStateOf(false)
    }

    val averageMark = remember {
        mutableStateOf(0.0)
    }

    Box(modifier = Modifier.fillMaxSize()){

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .padding(16.dp)
        ){
            item {
                BackButton {
                    if(places == null) {
                        navController.popBackStack()
                    }else{
                        val isCameraSet = true
                        val latitude = place.location.latitude
                        val longitude = place.location.longitude

                        val placesJson = Gson().toJson(places)
                        val encodedPlacesJson = URLEncoder.encode(placesJson, StandardCharsets.UTF_8.toString())
                        navController.navigate(Routes.indexScreen + "/$isCameraSet/$latitude/$longitude/$encodedPlacesJson")
                    }
                }
            }
            item{ PlaceLogoImage(imageUrl = place.logo, averageMark = averageMark)}
            item{ Spacer(modifier = Modifier.height(20.dp)) }
            item { Heading1Text(textValue = place.name.replace('+', ' ')) }
            item{ Spacer(modifier = Modifier.height(20.dp))}
            item{ AttendanceView(value = place.attendance)}
            item{ Spacer(modifier = Modifier.height(20.dp))}
            item{ LocationView(location = LatLng(place.location.latitude, place.location.longitude))}
            item{ Spacer(modifier = Modifier.height(20.dp))}
            item{ Text(text = place.description.replace('+', ' '))}
            item{ Spacer(modifier = Modifier.height(20.dp))}
            item { PlaceImagesView(images = place.images) }
            item{ Spacer(modifier = Modifier.height(60.dp))}
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 15.dp, vertical = 20.dp)
        ) {
            MarkButton(
                onClick = {
                      val markExist = marks.firstOrNull{
                          it.placeId == place.id && it.userId == authViewModel.currentUser?.uid
                      }
                    if(markExist != null)
                        myMark.value = markExist.mark.toString()
                    showMarkScreen.value = true
                },
                enabled =
                if(place.userId == authViewModel.currentUser?.uid)
                    false
                else
                    true,
                name = place.name
            )
        }



        if(showMarkScreen.value){
            AddMarkScreen(
                showMarkSreen = showMarkScreen,
                isLoading = isLoading,
                mark = myMark,
                onClick = {
                    val markExist = marks.firstOrNull{
                        it.placeId == place.id && it.userId == authViewModel.currentUser?.uid
                    }
                    if(markExist != null){
                        isLoading.value = true
                        placeViewModel.updateMark(markExist.id, myMark.value.toInt())
                    }
                    else{
                        isLoading.value = true
                        placeViewModel.addMark(
                            place.id,
                            myMark.value.toInt(),
                            place
                        )
                    }
                    showMarkScreen.value = false
                }
            )
        }
    }

    val marksResources = placeViewModel.marks.collectAsState()
    val newMarkResource = placeViewModel.newMark.collectAsState()

    marksResources.value.let {
        when(it){
            is Resource.Success -> {
                Log.e("Dobija", it.toString())
                marks.addAll(it.result)
                var sum = 0.0
                for (mark in it.result){
                    sum += mark.mark.toDouble()
                }
                if(sum != 0.0) {
                    val rawPositive = sum / it.result.count()
                    val rounded = rawPositive.toBigDecimal().setScale(1, RoundingMode.UP).toDouble()
                    averageMark.value = rounded
                }  else {}
            }
            is Resource.loading -> {

            }
            is Resource.Failure -> {
                Log.e("Podaci", it.toString())
            }
        }
    }
    newMarkResource.value.let {
        when(it){
            is Resource.Success -> {
                isLoading.value = false

                val markExist = marks.firstOrNull{mark ->
                    mark.id == it.result
                }
                if(markExist != null){
                    if(myMark.value != "")
                    markExist.mark = myMark.value.toInt()
                }
            }
            is Resource.loading -> {
            }
            is Resource.Failure -> {
                val context = LocalContext.current
                Toast.makeText(context, "Došlo je do greške prilikom ocenjivanja mesta", Toast.LENGTH_LONG).show()
                isLoading.value = false
            }
            null -> {
                isLoading.value = false
            }
        }
    }
}

