package com.example.nightvibe.screens

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.*
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nightvibe.models.Place
import com.example.nightvibe.models.User
import com.example.nightvibe.repositories.Resource
import com.example.nightvibe.ui.theme.lightGray
import com.example.nightvibe.ui.theme.lightMainColor
import com.example.nightvibe.ui.theme.mainColor
import com.example.nightvibe.viewmodels.AuthViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch
import java.math.RoundingMode
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FilterScreen(
    viewModel: AuthViewModel,
    places: MutableList<Place>,
    sheetState: ModalBottomSheetState,
    isFiltered: MutableState<Boolean>,
    isFilteredIndicator: MutableState<Boolean>,
    filteredPlace: MutableList<Place>,
    placeMarkers: MutableList<Place>,
    userLocation: LatLng?
) {
    val context = LocalContext.current

    viewModel.getAllUserData()
    val allUsersResource = viewModel.allUsers.collectAsState()

    val allUsersNames = remember {
        mutableListOf<String>()
    }

    val sharedPreferences = context.getSharedPreferences("filters", Context.MODE_PRIVATE)
    val options = sharedPreferences.getString("options", null)
    val attendance = sharedPreferences.getString("attendance", null)
    val range = sharedPreferences.getFloat("range", 1000f)

    val initialCheckedState = remember {
        mutableStateOf(List(allUsersNames.size) { false })
    }
    val rangeValues = remember { mutableFloatStateOf(1000f) }

    var selectedAttendance = remember {
        mutableListOf<Int>()
    }
    val filtersSet = remember {
        mutableStateOf(false)
    }

    if (isFilteredIndicator.value && attendance != null) {
        val type = object : TypeToken<List<Int>>() {}.type
        val savedCrowd: List<Int> = Gson().fromJson(attendance, type) ?: emptyList()
        selectedAttendance = savedCrowd.toMutableList()
    }
    if (isFilteredIndicator.value && options != null) {
        val type = object : TypeToken<List<Boolean>>() {}.type
        val savedOptions: List<Boolean> = Gson().fromJson(options, type) ?: emptyList()
        initialCheckedState.value = savedOptions
    }
    if(!filtersSet.value) {
        if (isFilteredIndicator.value) {
            rangeValues.floatValue = range
        }
        filtersSet.value = true
    }

    val allUsersData = remember {
        mutableListOf<User>()
    }

    val selectedOptions = remember {
        mutableStateOf(initialCheckedState.value)
    }

    val isSet = remember { mutableStateOf(false) }

    allUsersResource.value.let {
        when(it){
            is Resource.Failure -> {}
            is Resource.Success -> {
                allUsersNames.clear()
                allUsersData.clear()
                allUsersNames.addAll(it.result.map { user -> user.fullName})
                allUsersData.addAll(it.result)
                if(!isSet.value) {
                    initialCheckedState.value =
                        List(allUsersNames.count()) { false }.toMutableList()
                    isSet.value = true
                }
            }
            Resource.loading -> {}
            null -> {}
        }
    }
    val coroutineScope = rememberCoroutineScope()

    val expanded = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 40.dp, horizontal = 16.dp)
    ) {
        Text(
            text = "Autor",
            style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(modifier = Modifier.height(8.dp))


        Column{
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = { expanded.value = !expanded.value })
                    .background(lightGray, RoundedCornerShape(4.dp))
                    .padding(horizontal = 20.dp, vertical = 14.dp)
            ) {
                Text("Izaberi autore")
                Icon(
                    if (expanded.value) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                    contentDescription = "Dropdown icon"
                )
            }

            DropdownMenu(
                expanded = expanded.value,
                onDismissRequest = { expanded.value = false },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
            ) {
                allUsersNames.forEachIndexed { index, option ->
                    DropdownMenuItem(onClick = {
                        val updatedCheckedState = initialCheckedState.value.toMutableList()
                        updatedCheckedState[index] = !updatedCheckedState[index]
                        initialCheckedState.value = updatedCheckedState
                        selectedOptions.value = updatedCheckedState
                    },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Log.d("Checked", initialCheckedState.value[index].toString())
                            Checkbox(
                                checked = initialCheckedState.value[index],
                                onCheckedChange = {
                                    val updatedCheckedState = initialCheckedState.value.toMutableList()
                                    updatedCheckedState[index] = it
                                    initialCheckedState.value = updatedCheckedState
                                    selectedOptions.value = updatedCheckedState
                                }
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(option)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Gužva",
            style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        AttendanceSelector(selectedAttendance)
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Distanca",
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Text(
                text =
                if(rangeValues.floatValue != 1000f)
                    rangeValues.floatValue.toBigDecimal().setScale(1, RoundingMode.UP).toString() + "m"
                else
                    "Neograničeno"
                ,style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        RangeSliderExample(rangeValues = rangeValues)
        Spacer(modifier = Modifier.height(30.dp))
        CustomFilterButton {
            placeMarkers.clear()
            val filteredPlaces = places.toMutableList()

            Log.d("MojaLokacija", filteredPlaces.count().toString())
            if (rangeValues.floatValue != 1000f) {
                filteredPlaces.retainAll { place ->
                    calculateDistance(
                        userLocation!!.latitude,
                        userLocation.longitude,
                        place.location.latitude,
                        place.location.longitude
                    ) <= rangeValues.floatValue
                }
                with(sharedPreferences.edit()) {
                    putFloat("range", rangeValues.floatValue)
                    apply()
                }
            }

            if (selectedAttendance.isNotEmpty()) {
                filteredPlaces.retainAll { it.attendance in selectedAttendance }
                val crowdJson = Gson().toJson(selectedAttendance)
                with(sharedPreferences.edit()) {
                    putString("attendance", crowdJson)
                    apply()
                }
            }

            if (selectedOptions.value.indexOf(true) != -1) {
                val selectedAuthors = allUsersData.filterIndexed { index, _ ->
                    selectedOptions.value[index]
                }
                val selectedIndices = selectedAuthors.map { item -> item.id }
                Log.d("selektovani indices", selectedIndices.toString())
                filteredPlaces.retainAll { it.userId in selectedIndices }



                val selectedOptionsJson = Gson().toJson(selectedOptions.value)
                with(sharedPreferences.edit()) {
                    putString("options", selectedOptionsJson)
                    apply()
                }
            }
            //Log.d("filtrirana mesta", filteredPlaces.map { x -> x.name }.toString())
            filteredPlace.clear()
            filteredPlace.addAll(filteredPlaces)

            isFiltered.value = false
            isFiltered.value = true

            coroutineScope.launch {
                sheetState.hide()
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        CustomResetFilters {
            placeMarkers.clear()
            placeMarkers.addAll(places)

            initialCheckedState.value =
                List(allUsersNames.count()) { false }.toMutableList()
            rangeValues.floatValue = 1000f

            isFiltered.value = true
            isFiltered.value = false
            isFilteredIndicator.value = false

            with(sharedPreferences.edit()) {
                putFloat("range", 1000f)
                putString("attendance", null)
                putString("options", null)
                apply()
            }

            coroutineScope.launch {
                sheetState.hide()
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun AttendanceSelector(
    selected: MutableList<Int>
){
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        AttendanceOption("Slaba", 1, selected)
        AttendanceOption("Umerena", 2, selected)
        AttendanceOption("Velika", 3, selected)
    }
}

@Composable
fun AttendanceOption(
    text: String,
    index: Int,
    selected: MutableList<Int>
) {
    val isSelected = remember { mutableStateOf(selected.contains(index)) }

    Box(
        modifier = Modifier
            .background(
                if (isSelected.value) lightGray else Color.White,
                RoundedCornerShape(10.dp)
            )
            .border(
                width = 1.dp,
                color = if (isSelected.value) mainColor else lightGray,
                shape = RoundedCornerShape(10.dp)
            )
            .clickable {
                if (isSelected.value) {
                    selected.remove(index)
                } else {
                    selected.add(index)
                }
                isSelected.value = !isSelected.value
            }
            .padding(horizontal = 20.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = Icons.Filled.People, contentDescription = "")
            Spacer(modifier = Modifier.width(5.dp))
            Text(text = text)
        }
    }
}

@Composable
fun RangeSliderExample(
    rangeValues: MutableState<Float>
) {
    androidx.compose.material3.Slider(
        value = rangeValues.value,
        onValueChange = { rangeValues.value = it },
        valueRange = 0f..1000f,
        steps = 50,
        colors = SliderDefaults.colors(
            thumbColor = mainColor,
            activeTrackColor = lightMainColor,
            inactiveTrackColor = lightMainColor
        )
    )
}

@Composable
fun CustomFilterButton(
    onClick: () -> Unit
){
    androidx.compose.material3.Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(mainColor, RoundedCornerShape(30.dp)),
        colors = ButtonDefaults.buttonColors(
            containerColor = mainColor,
            contentColor = Color.Black,
            disabledContainerColor = lightMainColor,
            disabledContentColor = Color.White
        ),

        ) {
        Text(
            "Filtriraj",
            style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        )
    }
}

@Composable
fun CustomResetFilters(
    onClick: () -> Unit
){
    androidx.compose.material3.Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(mainColor, RoundedCornerShape(30.dp)),
        colors = ButtonDefaults.buttonColors(
            containerColor = lightMainColor,
            contentColor = Color.White,
            disabledContainerColor = lightMainColor,
            disabledContentColor = Color.White
        ),

        ) {
        Text(
            "Resetuj Filtere",
            style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        )
    }
}

private fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
    val earthRadius = 6371000.0

    val dLat = Math.toRadians(lat2 - lat1)
    val dLon = Math.toRadians(lon2 - lon1)

    val a = sin(dLat / 2) * sin(dLat / 2) +
            cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
            sin(dLon / 2) * sin(dLon / 2)

    val c = 2 * atan2(sqrt(a), sqrt(1 - a))

    return earthRadius * c
}