package com.example.nightvibe.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.nightvibe.models.Place
import com.example.nightvibe.models.User
import com.example.nightvibe.repositories.Resource
import com.example.nightvibe.screens.components.BackButton
import com.example.nightvibe.screens.components.GreyText
import com.example.nightvibe.screens.components.PhotosSection
import com.example.nightvibe.screens.components.ProfileImage
import com.example.nightvibe.ui.theme.goldColor
import com.example.nightvibe.ui.theme.mainColor
import com.example.nightvibe.viewmodels.AuthViewModel
import com.example.nightvibe.viewmodels.PlaceViewModel

@Composable
fun UserScreen(
    navController: NavController,
    placeViewModel: PlaceViewModel,
    user: User
) {
    placeViewModel.getUserPlaces(user.id)
    val placesResource = placeViewModel.userPlaces.collectAsState()
    val places = remember {
        mutableStateListOf<Place>()
    }

    placesResource.value.let {
        when(it){
            is Resource.Success -> {
                places.clear()
                places.addAll(it.result)
            }
            is Resource.loading -> {

            }
            is Resource.Failure -> {
                Log.e("Podaci", it.toString())
            }
            null -> {}
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .height(400.dp)
                        .background(
                            mainColor,
                            shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        ProfileImage(imageUrl = user.image)
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(
                            text = user.fullName.replace("+", " "),
                            color = Color.White,
                            fontSize = 22.sp
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "Titula:", color = Color.White, fontSize = 18.sp)
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                modifier = Modifier
                                    .background(
                                        goldColor,
                                        shape = RoundedCornerShape(10.dp)
                                    )
                                    .padding(10.dp),
                                text = if (user.score < 30) {
                                    "Ne pije"
                                } else if (user.score in 31..59) {
                                    "Može da popije"
                                } else {
                                    "Postaje nevidljiv"
                                },
                                color = Color.Gray,
                                fontSize = 15.sp,
                            )
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row {
                                Text(
                                    text = places.count().toString(),
                                    color = Color.White,
                                    fontSize = 15.sp,
                                    style = TextStyle(fontWeight = FontWeight.Bold)
                                )
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(
                                    text = "Dodatih mesta",
                                    color = Color.White,
                                    fontSize = 15.sp,
                                    style = TextStyle(fontWeight = FontWeight.Thin)
                                )
                            }
                            Surface(
                                modifier = Modifier
                                    .height(20.dp)
                                    .width(1.dp)
                                    .background(Color.White)
                            ) {}
                            Row {
                                Text(
                                    text = user.score.toString(),
                                    color = Color.White,
                                    fontSize = 15.sp,
                                    style = TextStyle(fontWeight = FontWeight.Bold)
                                )
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(
                                    text = "Broj poena",
                                    color = Color.White,
                                    fontSize = 15.sp,
                                    style = TextStyle(fontWeight = FontWeight.Thin)
                                )
                            }
                        }
                    }
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(16.dp)
                    ) {
                        BackButton {
                            navController.popBackStack()
                        }
                    }
                }
            }

            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Icon(imageVector = Icons.Filled.Phone, contentDescription = "")
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(text = user.phone)
                }
            }
            item { PhotosSection(places = places, navController = navController) }
        }
    }
}
