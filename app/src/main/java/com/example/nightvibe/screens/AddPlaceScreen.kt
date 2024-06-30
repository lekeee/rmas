package com.example.nightvibe.screens

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.nightvibe.navigation.Routes
import com.example.nightvibe.repositories.Resource
import com.example.nightvibe.screens.components.Attendance
import com.example.nightvibe.screens.components.GalleryForPlace
import com.example.nightvibe.screens.components.Heading1Text
import com.example.nightvibe.screens.components.ImageLogo
import com.example.nightvibe.screens.components.LabelForInput
import com.example.nightvibe.screens.components.LoginRegisterButton
import com.example.nightvibe.screens.components.SimpleTextInput
import com.example.nightvibe.screens.components.TextArea
import com.example.nightvibe.viewmodels.PlaceViewModel
import com.google.android.gms.maps.model.LatLng


@Composable
fun AddPlaceScreen(
    placeViewModel: PlaceViewModel?,
    location: MutableState<LatLng>?,
    navController: NavController
) {
    val placesFlow = placeViewModel?.placeFlow?.collectAsState()
    val selectedImage = remember {
        mutableStateOf<Uri?>(Uri.EMPTY)
    }
    val name = remember {
        mutableStateOf("")
    }
    val selectedAttendance = remember {
        mutableStateOf(0)
    }
    val description = remember {
        mutableStateOf("")
    }
    val selectedImages = remember {
        mutableStateOf<List<Uri>>(emptyList())
    }
    val buttonIsEnabled = remember {
        mutableStateOf(true)
    }
    val buttonIsLoading = remember {
        mutableStateOf(false)
    }

    val isAdded = remember {
        mutableStateOf(false)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 50.dp, horizontal = 20.dp)
    ) {
            item{ Heading1Text(textValue = "Dodaj novo mesto za provod")}
            item{ Spacer(modifier = Modifier.height(20.dp))}
            item{ LabelForInput(textValue = "Ime")}
            item{ SimpleTextInput(inputValue = name, inputText = "Unesite ime mesta")}
            item{ ImageLogo(selectedImageUri = selectedImage)}
            item{ LabelForInput(textValue = "Posecenost")}
            item{ Attendance(selected = selectedAttendance)}
            item{ Spacer(modifier = Modifier.height(20.dp))}
            item{ LabelForInput(textValue = "Opis")}
            item{ TextArea(inputValue = description, inputText = "Podelite sa ostalima informacije o mestu za provod")}
            item{ Spacer(modifier = Modifier.height(20.dp))}
            item{ LabelForInput(textValue = "Dodajte slike provoda na kojem ste bili")}
            item{ GalleryForPlace(selectedImages = selectedImages)}
            item{ Spacer(modifier = Modifier.height(10.dp))}
            item{
                LoginRegisterButton(icon = Icons.Filled.Add ,buttonText = "Dodaj mesto", isEnabled = buttonIsEnabled, isLoading = buttonIsLoading) {
                    isAdded.value = true
                buttonIsLoading.value = true
                placeViewModel?.savePlace(
                    name = name.value,
                    attendance = selectedAttendance.value,
                    description = description.value,
                    logo = selectedImage.value!!,
                    images = selectedImages.value,
                    location = location
                )
            }
        }
    }

    placesFlow?.value.let {
        when(it){
            is Resource.Failure -> {
                Log.d("Stanje flowa", it.toString());
                buttonIsLoading.value = false
                val context = LocalContext.current

                Toast.makeText(context, "Greska pri dodavanju", Toast.LENGTH_LONG).show()
            }
            is Resource.loading -> {

            }
            is Resource.Success -> {
                Log.d("Stanje flowa", it.toString())
                buttonIsLoading.value = false
                if(isAdded.value)
                    navController.navigate(Routes.indexScreen)
            }
            null -> {}
        }
    }
}
