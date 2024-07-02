package com.example.nightvibe.screens

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
import com.example.nightvibe.repositories.Resource
import com.example.nightvibe.screens.components.GreyText
import com.example.nightvibe.screens.components.Heading1Text
import com.example.nightvibe.ui.theme.lightYellow
import com.example.nightvibe.viewmodels.AuthViewModel

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



    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.White)
        .padding(12.dp, 20.dp, 20.dp, 20.dp)
        ){
        if(allUsers.isNotEmpty()){
            Column {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center){
                    Image(
                        painter = painterResource(id = R.drawable.crown),
                        contentDescription = "crown",
                        modifier = Modifier.size(130.dp)
                    )
                }
                Heading1Text(textValue = "Top 3 Ranking")
                Spacer(modifier = Modifier.height(20.dp))
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .background(
                        lightYellow,
                        shape = RoundedCornerShape(20.dp)
                    )
                ){
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Column(
                            modifier = Modifier.padding(10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.first_place),
                                contentDescription = "first_place",
                                modifier = Modifier.size(90.dp)
                            )
                            Spacer(modifier = Modifier.height(20.dp))
                            Row{
                                AsyncImage(model = allUsers[0].image, contentDescription = "first_place",modifier = Modifier
                                    .size(70.dp)
                                    .clip(CircleShape))
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(text = allUsers[0].fullName.replace("+", " "))
                            Text(
                                text = allUsers[0].score.toString() + "xp",
                                color = Color.Black,
                                style = TextStyle(fontSize = 10.sp)
                            )
                        }
                        Column(
                            modifier = Modifier.padding(10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.second_place),
                                contentDescription = "first_place",
                                modifier = Modifier.size(90.dp)
                            )
                            Spacer(modifier = Modifier.height(20.dp))
                            Row{
                                AsyncImage(model = allUsers[1].image, contentDescription = "first_place",modifier = Modifier
                                    .size(70.dp)
                                    .clip(CircleShape))
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(text = allUsers[1].fullName.replace("+", " "))
                            Text(
                                text = allUsers[1].score.toString() + "xp",
                                color = Color.Black,
                                style = TextStyle(fontSize = 10.sp)
                            )
                        }
//                    Column(
//                        modifier = Modifier.padding(10.dp),
//                        horizontalAlignment = Alignment.CenterHorizontally
//                    ) {
//                        Image(
//                            painter = painterResource(id = R.drawable.second_place),
//                            contentDescription = "first_place",
//                            modifier = Modifier.size(90.dp)
//                        )
//                        Spacer(modifier = Modifier.height(20.dp))
//                        Row{
//                            AsyncImage(model = allUsers[2].image, contentDescription = "first_place",modifier = Modifier
//                                .size(70.dp)
//                                .clip(CircleShape))
//                        }
//                        Spacer(modifier = Modifier.height(10.dp))
//                        Text(text = allUsers[2].fullName.replace("+", " "))
//                        Text(
//                            text = allUsers[2].score.toString(),
//                            color = Color.Black,
//                            style = TextStyle(fontSize = 10.sp)
//                        )
//                    }
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        3.dp,
                        lightYellow,
                        shape = RoundedCornerShape(20.dp)
                    )
                ){
                    Column {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp, 20.dp, 20.dp, 20.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            Text(text = "1", style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp))
                            Spacer(modifier = Modifier.width(20.dp))
                            Image(
                                painter = painterResource(id = R.drawable.logo),
                                contentDescription = "first_place",
                                modifier = Modifier
                                    .size(30.dp)
                                    .clip(CircleShape)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(text = "Aleksa")
                            Spacer(modifier = Modifier.width(40.dp))
                            Row(
                                horizontalArrangement = Arrangement.End,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "40 xp",
                                    color = Color.Black,
                                    style = TextStyle(fontSize = 12.sp)
                                )
                            }
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp, 0.dp, 20.dp, 20.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            Text(text = "2", style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp))
                            Spacer(modifier = Modifier.width(20.dp))
                            Image(
                                painter = painterResource(id = R.drawable.logo),
                                contentDescription = "first_place",
                                modifier = Modifier
                                    .size(30.dp)
                                    .clip(CircleShape)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(text = "Aleksa")
                            Spacer(modifier = Modifier.width(40.dp))
                            Row(
                                horizontalArrangement = Arrangement.End,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "40 xp",
                                    color = Color.Black,
                                    style = TextStyle(fontSize = 12.sp)
                                )
                            }
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp, 0.dp, 20.dp, 20.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            Text(text = "3", style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp))
                            Spacer(modifier = Modifier.width(20.dp))
                            Image(
                                painter = painterResource(id = R.drawable.logo),
                                contentDescription = "first_place",
                                modifier = Modifier
                                    .size(30.dp)
                                    .clip(CircleShape)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(text = "Aleksa")
                            Spacer(modifier = Modifier.width(40.dp))
                            Row(
                                horizontalArrangement = Arrangement.End,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "40 xp",
                                    color = Color.Black,
                                    style = TextStyle(fontSize = 12.sp)
                                )
                            }
                        }
                    }
                }
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp, 10.dp, 20.dp, 20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Text(text = "1", style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp))
                        Spacer(modifier = Modifier.width(20.dp))
                        Image(
                            painter = painterResource(id = R.drawable.logo),
                            contentDescription = "first_place",
                            modifier = Modifier
                                .size(30.dp)
                                .clip(CircleShape)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(text = "Aleksa")
                        Spacer(modifier = Modifier.width(40.dp))
                        Row(
                            horizontalArrangement = Arrangement.End,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "40 xp",
                                color = Color.Black,
                                style = TextStyle(fontSize = 12.sp)
                            )
                        }
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp, 0.dp, 20.dp, 20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Text(text = "1", style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp))
                        Spacer(modifier = Modifier.width(20.dp))
                        Image(
                            painter = painterResource(id = R.drawable.logo),
                            contentDescription = "first_place",
                            modifier = Modifier
                                .size(30.dp)
                                .clip(CircleShape)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(text = "Aleksa")
                        Spacer(modifier = Modifier.width(40.dp))
                        Row(
                            horizontalArrangement = Arrangement.End,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "40 xp",
                                color = Color.Black,
                                style = TextStyle(fontSize = 12.sp)
                            )
                        }
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp, 0.dp, 20.dp, 20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Text(text = "1", style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp))
                        Spacer(modifier = Modifier.width(20.dp))
                        Image(
                            painter = painterResource(id = R.drawable.logo),
                            contentDescription = "first_place",
                            modifier = Modifier
                                .size(30.dp)
                                .clip(CircleShape)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(text = "Aleksa")
                        Spacer(modifier = Modifier.width(40.dp))
                        Row(
                            horizontalArrangement = Arrangement.End,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "40 xp",
                                color = Color.Black,
                                style = TextStyle(fontSize = 12.sp)
                            )
                        }
                    }
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
