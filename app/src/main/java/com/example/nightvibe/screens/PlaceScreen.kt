package com.example.nightvibe.screens

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.nightvibe.models.Mark
import com.example.nightvibe.models.Place
import com.example.nightvibe.navigation.Routes
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
        mutableStateOf(0)
    }

    val showMarkScreen = remember {
        mutableStateOf(false)
    }

    val isLoading = remember {
        mutableStateOf(false)
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
            item{ PlaceLogoImage(imageUrl = place.logo)}
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
                        myMark.value = markExist.mark
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
                mark = myMark.value,
                onClick = {

                }
            )
        }
    }
}

