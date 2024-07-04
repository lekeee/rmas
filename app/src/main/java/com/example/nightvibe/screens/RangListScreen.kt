package com.example.nightvibe.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.nightvibe.R
import com.example.nightvibe.models.Place
import com.example.nightvibe.models.User
import com.example.nightvibe.navigation.Routes
import com.example.nightvibe.repositories.Resource
import com.example.nightvibe.screens.components.BackButton
import com.example.nightvibe.screens.components.GreyText
import com.example.nightvibe.screens.components.Heading1Text
import com.example.nightvibe.screens.components.OtherPlacesWidget
import com.example.nightvibe.screens.components.PlaceWidget
import com.example.nightvibe.ui.theme.lightYellow
import com.example.nightvibe.viewmodels.AuthViewModel
import com.google.gson.Gson
import okhttp3.internal.notify
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun RangListScreen(
    viewModel: AuthViewModel?,
    navController: NavController?
) {
    viewModel?.getAllUserData()
    val allUsersResource = viewModel?.allUsers?.collectAsState()

    val allUsers = remember {
        mutableListOf<User>()
    }
    val placeMarkers = remember {
        mutableStateListOf<Place>()
    }



    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(12.dp, 20.dp, 20.dp, 20.dp)
    ) {
        if (allUsers.isNotEmpty()) {
            Column {
                BackButton {
                    navController?.popBackStack()
                }
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {

                    Image(
                        painter = painterResource(id = R.drawable.crown),
                        contentDescription = "crown",
                        modifier = Modifier.size(130.dp)
                    )
                }
                Heading1Text(textValue = "Top 3 Ranking")
                Spacer(modifier = Modifier.height(20.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .background(
                            lightYellow,
                            shape = RoundedCornerShape(20.dp)
                        )
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        if (allUsers.size > 0) {
                            allUsers.forEachIndexed { index, user ->
                                var icon = 0
                                if (index in 0..2) {
                                    when (index) {
                                        0 -> icon = R.drawable.first_place
                                        1 -> icon = R.drawable.second_place
                                        2 -> icon = R.drawable.third_place

                                    }
                                    PlaceWidget(index = index, user = allUsers[index], icon = icon){
                                        val userJson = Gson().toJson(user)
                                        val encodedUserJson = URLEncoder.encode(userJson, StandardCharsets.UTF_8.toString())
                                        navController?.navigate(Routes.userScreen + "/${encodedUserJson}")
                                    }
                                }
                            }
                        }
                    }
                }
                if(allUsers.size > 3){
                    val otherUsers = allUsers.drop(3)
                    Log.d("otherusers", otherUsers.toString())
                    OtherPlacesWidget(users = otherUsers, navController)
                }
            }
        }
    }
    allUsersResource?.value.let {
        when(it){
            is Resource.Failure -> {}
            is Resource.Success -> {
                allUsers.clear()
                allUsers.addAll(it.result.sortedByDescending { x -> x.score })
            }
            Resource.loading -> {}
            null -> {}
        }
    }
}
