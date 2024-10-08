package com.example.nightvibe.screens

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PeopleAlt
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.NavController
import com.example.nightvibe.R
import com.example.nightvibe.models.Place
import com.example.nightvibe.models.User
import com.example.nightvibe.navigation.Routes
import com.example.nightvibe.repositories.Resource
import com.example.nightvibe.screens.components.LoginRegisterButton
import com.example.nightvibe.screens.components.PlaceMarker
import com.example.nightvibe.screens.components.SearchBar
import com.example.nightvibe.screens.components.UserImage
import com.example.nightvibe.screens.components.myPositionIndicator
import com.example.nightvibe.services.LocationService
import com.example.nightvibe.ui.theme.mainColor
import com.example.nightvibe.viewmodels.AuthViewModel
import com.example.nightvibe.viewmodels.PlaceViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import kotlinx.coroutines.launch
import java.net.URLEncoder
import java.nio.charset.StandardCharsets


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun IndexScreen(
    navController: NavController,
    viewModel: AuthViewModel,
    cameraPosition : CameraPositionState = rememberCameraPositionState(){
        position = CameraPosition.fromLatLngZoom(LatLng(42.96509503785226, 22.131214613702426), 17f)
    },
    isCameraSet: MutableState<Boolean> = remember {
        mutableStateOf(false)
    },
    placeViewModel: PlaceViewModel,
    placesMarkers: MutableList<Place>
){
    viewModel.getUserData()
    val userDataResource = viewModel.currentUserFlow.collectAsState()
    val user = remember {
        mutableStateOf<User?>(null)
    }
    val markers = remember { mutableStateListOf<LatLng>() }
    val properties = remember {
        mutableStateOf(MapProperties(mapType = MapType.TERRAIN))
    }
    val uiSettings = remember { mutableStateOf(MapUiSettings(zoomControlsEnabled = false)) }

    val myLocation = remember {
        mutableStateOf<LatLng?>(null)
    }

    val buttonIsEnabled = remember { mutableStateOf(true) }
    val isLoading = remember { mutableStateOf(false) }

    val placesData = placeViewModel.places.collectAsState()
    val allPlaces = remember {
        mutableListOf<Place>()
    }
    placesData.value.let {
        when(it){
            is Resource.Success -> {
                allPlaces.clear()
                allPlaces.addAll(it.result)
            }
            is Resource.loading -> {

            }
            is Resource.Failure -> {
                Log.e("Podaci", it.toString())
            }
            null -> {}
        }
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

    val filtersOn = remember {
        mutableStateOf(false)
    }
    val filteredPlaces = remember {
        mutableListOf<Place>()
    }

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

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    // State to control gesturesEnabled
    val gesturesEnabled = remember { mutableStateOf(false) }

    // Monitor the drawer state and update gesturesEnabled accordingly
    LaunchedEffect(drawerState.isOpen) {
        gesturesEnabled.value = drawerState.isOpen
    }

    val inputValue = remember {
        mutableStateOf("")
    }

    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)

    val isFilteredIndicator = remember{
        mutableStateOf(false)
    }

    var placesToShow = remember {
        mutableListOf<Place>()
    }

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = {
            FilterScreen(
                viewModel = viewModel,
                places = allPlaces,
                sheetState = sheetState,
                isFiltered = filtersOn,
                isFilteredIndicator = isFilteredIndicator,
                filteredPlace = filteredPlaces,
                placeMarkers = placesMarkers,
                userLocation = myLocation.value
            )
        },
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        modifier = Modifier.fillMaxSize()
    ){
        Column(modifier = Modifier
            .fillMaxSize()
            .height(100.dp),)
        {
            ModalNavigationDrawer(
                drawerState = drawerState,
                gesturesEnabled = gesturesEnabled.value,
                drawerContent = {
                    ModalDrawerSheet {
                        Box(
                            modifier = Modifier
                                .background(mainColor)
                                .fillMaxWidth()
                                .height(140.dp)
                        ){
                            if(user.value != null)
                                UserImage(imageUrl = user.value!!.image, name = user.value!!.fullName, score = user.value!!.score)
                        }
                        HorizontalDivider()
                        NavigationDrawerItem(
                            label = { Text(text = "Profil", color = Color.Black) },
                            selected = false,
                            icon = { Icon(imageVector = Icons.Filled.AccountCircle, contentDescription = "profile", tint = Color.Gray) },
                            onClick = {
                                coroutineScope.launch {
                                    drawerState.close()
                                    val userJson = Gson().toJson(user.value)
                                    val encodedUserJson = URLEncoder.encode(userJson, StandardCharsets.UTF_8.toString())
                                    navController.navigate(Routes.userScreen + "/$encodedUserJson")
                                }
                            }
                        )
                        NavigationDrawerItem(
                            label = { Text(text = "Mesta", color = Color.Black) },
                            selected = false,
                            icon = { Icon(imageVector = Icons.Filled.Place, contentDescription = "places", tint = Color.Gray) },
                            onClick = {
                                coroutineScope.launch {
                                    drawerState.close()
                                    val placesJson = Gson().toJson(
                                        if(filtersOn.value)
                                            filteredPlaces
                                        else
                                            placesMarkers
                                    )
                                    val encodedPlacesJson = URLEncoder.encode(placesJson, StandardCharsets.UTF_8.toString())
                                    navController.navigate(Routes.placesScreen + "/$encodedPlacesJson")
                                }
                            }
                        )
                        NavigationDrawerItem(
                            label = { Text(text = "Rang lista", color = Color.Black) },
                            selected = false,
                            icon = { Icon(imageVector = Icons.Filled.PeopleAlt, contentDescription = "list", tint = Color.Gray) },
                            onClick = {
                                coroutineScope.launch {
                                    drawerState.close()
                                    navController.navigate(Routes.rangListScreen)
                                }
                            }
                        )
                        NavigationDrawerItem(
                            label = { Text(text = "Podesavanja", color = Color.Black) },
                            selected = false,
                            icon = { Icon(imageVector = Icons.Filled.Settings, contentDescription = "settings", tint = Color.Gray) },
                            onClick = {
                                coroutineScope.launch {
                                    drawerState.close()
                                    navController.navigate(Routes.settingsScreen)
                                }
                            }
                        )
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.End,
                            verticalArrangement = Arrangement.Bottom
                        ) {
                            Box(modifier = Modifier.padding(10.dp)){
                                LoginRegisterButton(
                                    buttonText = "Odjavi se",
                                    icon = Icons.Filled.Logout,
                                    isEnabled = buttonIsEnabled,
                                    isLoading = isLoading
                                ) {
                                    viewModel.logout()
                                    navController.navigate(Routes.loginScreen)
                                }
                            }
                        }
                    }
                },
            ) {
                Box(modifier = Modifier.fillMaxSize()){
                    GoogleMap(
                        modifier = Modifier.fillMaxSize(),
                        cameraPositionState = cameraPosition,
                        properties = properties.value,
                        uiSettings = uiSettings.value,
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
                        if(!filtersOn.value) {
                            placesMarkers.forEach { place ->
                                val icon = myPositionIndicator(
                                    context, R.drawable.beverage_cocktail
                                )
                                PlaceMarker(
                                    place = place,
                                    icon = icon,
                                    placesMarkers = placesMarkers,
                                    navController = navController,
                                    notFiltered = true
                                )
                            }
                        }
                        else{
                            Log.d("filtrirana mesta", filteredPlaces.map { x -> x.name }.toString())
                            filteredPlaces.forEach{ place ->
                                val icon = myPositionIndicator(
                                    context, R.drawable.beverage_cocktail
                                )
                                PlaceMarker(
                                    place = place,
                                    icon = icon,
                                    placesMarkers = filteredPlaces,
                                    navController = navController,
                                    notFiltered = false
                                )
                            }
                        }
                    }
                    Column {
                        Spacer(modifier = Modifier.height(15.dp))
                        Row {
                            Spacer(modifier = Modifier.width(15.dp))
                            Box(
                                modifier = Modifier.background(
                                    Color.White,
                                    shape = RoundedCornerShape(10.dp)
                                )
                            ){
                                IconButton(
                                    onClick = {
                                        coroutineScope.launch {
                                            drawerState.open()
                                        }
                                    },
                                    modifier = Modifier
                                        .width(50.dp)
                                        .height(50.dp)
                                        .border(
                                            1.dp,
                                            mainColor,
                                            shape = RoundedCornerShape(10.dp)
                                        )
                                        .clip(
                                            RoundedCornerShape(10.dp)
                                        ),
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Menu,
                                        contentDescription = "Menu",
                                        tint = mainColor
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(5.dp))
                            SearchBar(inputValue = inputValue, placesData = placesMarkers, cameraPositionState = cameraPosition, navController = navController)
                            Spacer(modifier = Modifier.width(5.dp))

                            Box(
                                modifier = Modifier.background(
                                    mainColor,
                                    shape = RoundedCornerShape(10.dp)
                                )
                            ){
                                IconButton(
                                    onClick = {
                                        coroutineScope.launch {
                                            sheetState.show()
                                        }
                                    },
                                    modifier = Modifier
                                        .width(50.dp)
                                        .height(50.dp)
                                        .clip(
                                            RoundedCornerShape(10.dp)
                                        ),
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.FilterList,
                                        contentDescription = "Filter",
                                        tint = Color.White
                                    )
                                }
                            }

                        }
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.End,
                            verticalArrangement = Arrangement.Bottom
                        ) {

                            Row {
                                Box(
                                    modifier = Modifier.background(
                                        mainColor,
                                        shape = RoundedCornerShape(10.dp)
                                    )
                                ){
                                    IconButton(
                                        onClick = {
                                            navController.navigate(route = Routes.addPlaceScreen + "/${myLocation.value!!.latitude}/${myLocation.value!!.longitude}")
                                        },
                                        modifier = Modifier
                                            .width(50.dp)
                                            .height(50.dp)
                                            .clip(
                                                RoundedCornerShape(10.dp)
                                            ),
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Add,
                                            contentDescription = "Add",
                                            tint = Color.White
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.width(15.dp))
                            }
                            Spacer(modifier = Modifier.height(15.dp))
                        }
                    }
                }
            }
        }
    }


    userDataResource.value.let {
        when(it){
            is Resource.Success -> {
                user.value = it.result
            }
            null -> {
                user.value = null
            }

            is Resource.Failure -> {}
            Resource.loading -> {}
        }
    }

}