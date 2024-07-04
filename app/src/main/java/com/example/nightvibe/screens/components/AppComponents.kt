package com.example.nightvibe.screens.components

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.DoNotDisturb
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.ShareLocation
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.room.util.query
import coil.compose.AsyncImage
import com.example.nightvibe.R
import com.example.nightvibe.models.Place
import com.example.nightvibe.models.User
import com.example.nightvibe.navigation.Routes
import com.example.nightvibe.ui.theme.buttonDisabledColor
import com.example.nightvibe.ui.theme.goldColor
import com.example.nightvibe.ui.theme.greyTextColor
import com.example.nightvibe.ui.theme.lightGray
import com.example.nightvibe.ui.theme.lightYellow
import com.example.nightvibe.ui.theme.mainColor
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberMarkerState
import java.net.URLEncoder
import java.nio.charset.StandardCharsets


@Composable
fun Heading1Text(textValue: String){
    Text(style = TextStyle(
        color = Color.Black,
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center
    ),
        modifier = Modifier.fillMaxWidth(),
        text = textValue
    )
}

@Composable
fun GreyText(textValue: String){
    Text(style = TextStyle(
        color = greyTextColor,
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium,
        textAlign = TextAlign.Center
    ),
        modifier = Modifier.fillMaxWidth(),
        text = textValue
    )
}

@Composable
fun LabelForInput(textValue: String){
    Text(style = TextStyle(
        color = Color.Black,
        fontSize = 13.sp,
        fontWeight = FontWeight.Medium
    ),
        text = textValue
    )
}

@Composable
fun NightOutImage(){
    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(20.dp), contentAlignment = Alignment.Center){
        Image(painter = painterResource(id = R.drawable.nightout),
            contentDescription = "Login Image",
            modifier = Modifier
                .width(210.dp)
                .height(210.dp)
            )
    }
}

@Composable
fun UploadIcon(
    selectedImageUri: MutableState<Uri?>,
    isError: MutableState<Boolean>

) {
    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            selectedImageUri.value = uri
        }
    )

    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp),
        contentAlignment = Alignment.Center
    ) {
        if (selectedImageUri.value == Uri.EMPTY || selectedImageUri.value == null) {
            Image(
                painter = painterResource(id = R.drawable.plus),
                contentDescription = "Profile Image",
                modifier = Modifier
                    .size(140.dp)
                    .border(
                        if (isError.value) BorderStroke(2.dp, Color.Red) else BorderStroke(
                            0.dp,
                            Color.Transparent
                        )
                    )
                    .clip(RoundedCornerShape(70.dp)) // 50% border radius
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        singlePhotoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }
            )
        } else {
            selectedImageUri.value?.let { uri ->
                Image(
                    painter = painterResource(id = R.drawable.plus),
                    contentDescription = "Profile Image",
                    modifier = Modifier
                        .size(140.dp)
                        .border(
                            if (isError.value) BorderStroke(2.dp, Color.Red) else BorderStroke(
                                0.dp,
                                Color.Transparent
                            )
                        )
                        .clip(RoundedCornerShape(70.dp)) // 50% border radius
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {
                            singlePhotoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        }
                )

                AsyncImage(
                    model = uri,
                    contentDescription = null,
                    modifier = Modifier
                        .size(140.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.LightGray)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {
                            singlePhotoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        },
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}
@Composable
fun TextInput(
    isEmail: Boolean,
    isNumber: Boolean = false,
    inputValue: MutableState<String>,
    inputText: String,
    leadingIcon: ImageVector,
    isError: MutableState<Boolean>,
    errorText: MutableState<String>
){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
            .shadow(
                6.dp,
                shape = RoundedCornerShape(10.dp)
            )
            .border(
                1.dp,
                if (isError.value) Color.Red else Color.Transparent,
                shape = RoundedCornerShape(10.dp)
            )
            .background(
                Color.White,
                shape = RoundedCornerShape(10.dp)
            )
    ){
        OutlinedTextField(
            value = inputValue.value,
            onValueChange = { newValue ->
                inputValue.value = newValue
                isError.value = false
            },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(
                    text = inputText,
                    style = TextStyle(
                        color = greyTextColor,
                        fontWeight = FontWeight.Medium
                    )
                )
            },
            leadingIcon = {
                Icon(imageVector = leadingIcon,
                    contentDescription = "",
                    tint = Color.Black)
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
            ),
            keyboardOptions = if(isEmail && !isNumber) KeyboardOptions(keyboardType = KeyboardType.Email) else if(!isEmail && isNumber) KeyboardOptions(keyboardType = KeyboardType.Number) else KeyboardOptions.Default
        )
    }
    if(isError.value && errorText.value.isNotEmpty()) {
        Text(
            text = errorText.value,
            modifier = Modifier.fillMaxWidth(),
            style = TextStyle(
                textAlign = TextAlign.Center,
                color = Color.Red
            )
        )
    }
}

@Composable
fun PasswordInput(
    inputValue: MutableState<String>,
    inputText: String,
    leadingIcon: ImageVector,
    isError: MutableState<Boolean>,
    errorText: MutableState<String>
){
    var showPassword = remember {
        mutableStateOf(false)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
            .shadow(
                6.dp,
                shape = RoundedCornerShape(10.dp)
            )
            .border(
                1.dp,
                if (isError.value) Color.Red else Color.Transparent,
                shape = RoundedCornerShape(10.dp)
            )
            .background(
                Color.White,
                shape = RoundedCornerShape(10.dp)
            )
    ){
        OutlinedTextField(
            value = inputValue.value,
            onValueChange = { newValue ->
                inputValue.value = newValue
                isError.value = false
            },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(
                    text = inputText,
                    style = TextStyle(
                        color = greyTextColor,
                        fontWeight = FontWeight.Medium
                    )
                )
            },
            leadingIcon = {
                Icon(imageVector = leadingIcon,
                    contentDescription = "",
                    tint = Color.Black)
            },
            trailingIcon = {
                IconButton(onClick = {
                    showPassword.value = !showPassword.value
                }) {
                    Icon(
                        imageVector = if(!showPassword.value) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                        contentDescription = "",
                        tint = Color.Black
                    )
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
            ),
            visualTransformation = if(!showPassword.value) PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )
    }
    if(isError.value && errorText.value.isNotEmpty()) {
        Text(
            text = errorText.value,
            modifier = Modifier.fillMaxWidth(),
            style = TextStyle(
                textAlign = TextAlign.Center,
                color = Color.Red
            )
        )
    }else{
        Text(text = " ")
    }
}

@Composable
fun LoginRegisterButton(
    buttonText: String,
    icon: ImageVector,
    isEnabled: MutableState<Boolean>,
    isLoading: MutableState<Boolean>,
    onClick: () -> Unit
){
    Button(
        onClick = onClick,
        modifier = Modifier
            .padding(vertical = 2.dp)
            .height(50.dp)
            .border(2.dp, mainColor, RoundedCornerShape(10.dp))
        ,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = mainColor,
            disabledContainerColor = buttonDisabledColor,
            disabledContentColor = Color.White
        ),
        shape = RoundedCornerShape(10.dp),
        enabled = isEnabled.value

    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            if(!isLoading.value){
                Icon(imageVector = icon, contentDescription = null)
                Spacer(modifier = Modifier.width(10.dp))
            }
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxHeight()
            ) {
                if (isLoading.value) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = mainColor,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = buttonText,
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun customClickableText(
    firstText: String,
    secondText: String,
    onClick: () -> Unit
){
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = firstText,
            style = TextStyle(
                fontSize = 12.sp
            )
        )
        Text(
            text = secondText,
            modifier = Modifier
                .clickable {
                    onClick()
                }
                .padding(start = 4.dp),
            style = TextStyle(
                fontSize = 12.sp,
                color = mainColor
            )
        )
    }
}

@Composable
fun customErrorContainer(
    errorText: String
){
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(50.dp),
        contentAlignment = Alignment.Center
    ){
        Text(
            text = errorText,
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                color = Color.Red
            )
        )
    }
}

fun myPositionIndicator(
    context: Context,
    vectorResId: Int
): BitmapDescriptor? {

    // retrieve the actual drawable
    val drawable = ContextCompat.getDrawable(context, vectorResId) ?: return null
    drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
    val bm = Bitmap.createBitmap(
        drawable.intrinsicWidth,
        drawable.intrinsicHeight,
        Bitmap.Config.ARGB_8888
    )

    // draw it onto the bitmap
    val canvas = android.graphics.Canvas(bm)
    drawable.draw(canvas)
    return BitmapDescriptorFactory.fromBitmap(bm)
}

@Composable
fun ImageLogo(
    selectedImageUri: MutableState<Uri?>
){
    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            selectedImageUri.value = uri
        }
    )

    val interactionSource = remember { MutableInteractionSource() }
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(200.dp)
        .border(
            1.dp,
            Color.Transparent,
            shape = RoundedCornerShape(20.dp)
        ),
        contentAlignment = Alignment.Center,
    ){
        if (selectedImageUri.value == Uri.EMPTY || selectedImageUri.value == null) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        singlePhotoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.image_upload),
                    modifier = Modifier
                        .width(100.dp)
                        .height(100.dp),
                    contentDescription = ""
                )
                Text(text = "Dodaj logo")
            }
        }else{
            selectedImageUri.value?.let { uri ->
                AsyncImage(
                    model = uri,
                    contentDescription = null,
                    modifier = Modifier
                        .width(150.dp)
                        .height(150.dp)
                        .background(Color.LightGray)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {
                            singlePhotoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        },
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}

@Composable
fun GalleryForPlace(
    selectedImages: MutableState<List<Uri>>
) {
    val pickImagesLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris ->
            selectedImages.value += uris
    }

    LazyRow {
        if (selectedImages.value.size < 4) {
            item {
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .width(100.dp)
                        .height(100.dp)
                        .border(
                            1.dp,
                            mainColor,
                            shape = RoundedCornerShape(10.dp),
                        )
                        .clickable { pickImagesLauncher.launch("image/*") },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = Icons.Filled.Add, contentDescription = "")
                }
            }
        }
        items(selectedImages.value.size) { index ->
            val uri = selectedImages.value[index]
            Box(
                modifier = Modifier
                    .padding(4.dp)
                    .width(100.dp)
                    .height(100.dp)
                    .border(
                        1.dp,
                        Color.Transparent,
                        shape = RoundedCornerShape(10.dp),
                    )
                    .background(
                        Color.White,
                        shape = RoundedCornerShape(10.dp),
                    )
                    .clickable { selectedImages.value -= uri },
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = uri,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(10.dp))
                )
            }
        }
    }
}

@Composable
fun SimpleTextInput(
    inputValue: MutableState<String>,
    inputText: String,
){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
            .border(
                1.dp,
                Color.Transparent,
                shape = RoundedCornerShape(10.dp)
            )
            .background(
                Color.White,
                shape = RoundedCornerShape(10.dp)
            )
    ){
        OutlinedTextField(
            value = inputValue.value,
            onValueChange = { newValue ->
                inputValue.value = newValue
            },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(
                    text = inputText,
                    style = TextStyle(
                        color = greyTextColor,
                        fontWeight = FontWeight.Medium
                    )
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
            )
        )
    }
}

@Composable
fun Attendance(
    selected: MutableState<Int>
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(30.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Surface(
            if(selected.value == 1)
                Modifier
                    .background(greyTextColor, shape = RoundedCornerShape(10.dp))
                    .padding(10.dp)
            else
                Modifier
                    .background(Color.Transparent)
                    .padding(10.dp)
        ) {
            Image(
                modifier =  if(selected.value == 1)
                    Modifier.background(greyTextColor)
                else
                    Modifier
                        .background(Color.Transparent)
                        .clickable { selected.value = 1 },
                painter = painterResource(id = R.drawable.person_maincolor_1),
                contentDescription = ""
            )
        }
        Surface(
            if(selected.value == 2)
                Modifier
                    .background(greyTextColor, shape = RoundedCornerShape(10.dp))
                    .padding(10.dp)
            else
                Modifier
                    .background(Color.Transparent)
                    .padding(10.dp)
        ) {
            Image(
                modifier = if(selected.value == 2)
                    Modifier.background(greyTextColor)
                else
                    Modifier
                        .background(Color.Transparent)
                        .clickable { selected.value = 2 },
                painter = painterResource(id = R.drawable.person_maincolor_2),
                contentDescription = ""
            )
        }
        Surface(
            if(selected.value == 3)
                Modifier
                    .background(greyTextColor, shape = RoundedCornerShape(10.dp))
                    .padding(10.dp)
            else
                Modifier
                    .background(Color.Transparent)
                    .padding(10.dp)
        ) {
            Image(
                modifier =  if(selected.value == 3)
                                Modifier.background(greyTextColor)
                            else
                    Modifier
                        .background(Color.Transparent)
                        .clickable { selected.value = 3 },
                painter = painterResource(id = R.drawable.person_maincolor_3),
                contentDescription = ""
            )
        }
    }
}

@Composable
fun TextArea(
    inputValue: MutableState<String>,
    inputText: String,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
            .border(
                1.dp,
                mainColor,
                shape = RoundedCornerShape(10.dp)
            )
            .background(
                Color.White,
                shape = RoundedCornerShape(10.dp)
            )
    ) {
        OutlinedTextField(
            value = inputValue.value,
            onValueChange = { newValue ->
                inputValue.value = newValue
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            placeholder = {
                Text(
                    text = inputText,
                    style = TextStyle(
                        color = greyTextColor,
                        fontWeight = FontWeight.Medium
                    )
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
            ),
            keyboardOptions = KeyboardOptions.Default
        )
    }
}

@Composable
fun PlaceLogoImage(
    imageUrl: String,
    averageMark: MutableState<Double>
){
    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(top = 20.dp), contentAlignment = Alignment.Center){
        Box(
            contentAlignment = Alignment.TopEnd
        ){
            AsyncImage(
                model = imageUrl,
                contentDescription = "",
                modifier = Modifier
                    .width(200.dp)
                    .height(200.dp)
                    .border(
                        1.dp,
                        Color.Gray,
                        shape = RoundedCornerShape(100.dp)
                    )
                    .shadow(
                        6.dp,
                        shape = RoundedCornerShape(100.dp)
                    )
                    .clip(shape = RoundedCornerShape(100.dp)),
                contentScale = ContentScale.Crop
            )
            Text(
                modifier = Modifier
                    .background(
                        goldColor,
                        shape = RoundedCornerShape(5.dp)
                    )
                    .padding(10.dp),
                text = "${averageMark.value}/10",
                color = Color.Black,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }

    }
}

@Composable
fun BackButton(
    onClick: () -> Unit
){
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .background(
                Color.Transparent,
                RoundedCornerShape(5.dp)
            )
            .padding(0.dp)
    ) {
        Icon(
            imageVector = Icons.Filled.ArrowBackIosNew,
            contentDescription = ""
        )
    }
}

@Composable
fun LocationView(
    location: LatLng
){
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Filled.LocationOn,
            contentDescription = "",
            tint = mainColor
        )

        Text(style = TextStyle(
            color = greyTextColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center
        ),
            text = "${location.latitude}, ${location.longitude}"
        )
    }
}

@Composable
fun AttendanceView(
    value: Int
){
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if(value == 1) {
            Image(
                painter = painterResource(id = R.drawable.person_maincolor_1),
                contentDescription = ""
            )
            Spacer(modifier = Modifier.height(10.dp))
            GreyText(textValue = "Posecenost 100-200 ljudi")
        }
        if(value == 2) {
            Image(
                painter = painterResource(id = R.drawable.person_maincolor_2),
                contentDescription = ""
            )
            Spacer(modifier = Modifier.height(10.dp))
            GreyText(textValue = "Posecenost 200-1000 ljudi ")
        }
        if(value == 3) {
            Image(
                painter = painterResource(id = R.drawable.person_maincolor_3),
                contentDescription = ""
            )
            Spacer(modifier = Modifier.height(10.dp))
            GreyText(textValue = "Posecenost 1000-4000 ljudi ")
        }
        Spacer(modifier = Modifier.width(20.dp))
    }
}

@Composable
fun PlaceImagesView(
    images: List<String>
){
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        for (index in images.indices step 2) {
            Row(modifier = Modifier.fillMaxWidth()) {
                AsyncImage(
                    model = images[index],
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(170.dp)
                )
                Spacer(modifier = Modifier.width(5.dp))
                if (index + 1 < images.size) {
                    AsyncImage(
                        model = images[index + 1],
                        contentDescription = "",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(170.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(5.dp))

        }
    }
}

@Composable
fun MarkButton(
    onClick: () -> Unit,
    enabled: Boolean,
    name: String
){
    Button(
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = mainColor,
            contentColor = Color.Black,
            disabledContainerColor = buttonDisabledColor,
            disabledContentColor = Color.White
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(mainColor, RoundedCornerShape(30.dp)),

        ) {
        Text(
            "Oceni ${name.replace("+", " ")}",
            style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        )
    }
}

@Composable
fun UserImage(
    imageUrl: String,
    name: String,
    score: Int
){
    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(top = 20.dp), contentAlignment = Alignment.Center){
        Row {
            AsyncImage(
                model = imageUrl,
                contentDescription = "userimage",
                modifier = Modifier
                    .width(100.dp)
                    .height(100.dp)
                    .border(
                        3.dp,
                        Color.Gray,
                        shape = RoundedCornerShape(100.dp)
                    )
                    .shadow(
                        6.dp,
                        shape = RoundedCornerShape(100.dp)
                    )
                    .clip(shape = RoundedCornerShape(100.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(40.dp))
            Column {
                Text(
                    modifier = Modifier
                        .background(
                            goldColor,
                            shape = RoundedCornerShape(5.dp)
                        )
                        .padding(10.dp),
                    text = name.replace(","," "),
                    color = Color.Black,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Titula:")
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        style = TextStyle(
                        color = greyTextColor,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center
                    ),
                        text = if(score < 30){
                            "Ne pije"
                        }
                        else if(score in 31..59){
                            "Moze da popije"
                        }
                        else{
                            "Postaje nevidljiv"
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ProfileImage(
    imageUrl: String
){
    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(top = 20.dp), contentAlignment = Alignment.Center){
        Row {
            AsyncImage(
                model = imageUrl,
                contentDescription = "userimage",
                modifier = Modifier
                    .width(200.dp)
                    .height(200.dp)
                    .border(
                        3.dp,
                        Color.Gray,
                        shape = RoundedCornerShape(100.dp)
                    )
                    .shadow(
                        6.dp,
                        shape = RoundedCornerShape(100.dp)
                    )
                    .clip(shape = RoundedCornerShape(100.dp)),
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Composable
fun PhotosSection(
    places: List<Place>,
    navController: NavController
) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        Text(text = "Dodate plaže")
        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            if(places.isNotEmpty()) {
                for (place in places) {
                    item {
                        AsyncImage(
                            model = place.logo,
                            contentScale = ContentScale.Crop,
                            contentDescription = "",
                            modifier =
                            Modifier
                                .width(150.dp)
                                .height(150.dp)
                                .clip(RoundedCornerShape(20.dp))
                                .background(
                                    Color.White,
                                    RoundedCornerShape(20.dp)
                                )
                                .clickable {
                                    val placeJson = Gson().toJson(place)
                                    val encodedPlaceJson = URLEncoder.encode(
                                        placeJson,
                                        StandardCharsets.UTF_8.toString()
                                    )
                                    navController.navigate(Routes.placeScreen + "/$encodedPlaceJson")
                                }
                        )
                    }
                }
            }else{
                item {
                    Image(
                        imageVector = Icons.Filled.DoNotDisturb,
                        contentScale = ContentScale.Crop,
                        contentDescription = "",
                        modifier =
                        Modifier
                            .width(150.dp)
                            .height(150.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(
                                Color.Gray,
                                RoundedCornerShape(20.dp)
                            )
                    )
                }
            }
        }
    }
}

@Composable
fun PlaceRow(
    place: Place,
    placeScreen: () -> Unit,
    placeOnMap: () -> Unit
){
    val boxModifier = Modifier.padding(12.dp)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                lightGray,
                shape = RoundedCornerShape(10.dp)

            )
            .clickable { placeScreen() }
            .border(
                1.dp,
                mainColor,
                shape = RoundedCornerShape(10.dp)
            )
            .padding(5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.width(60.dp)){
            AsyncImage(
                model = place.logo,
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .height(60.dp)
                    .clip(RoundedCornerShape(5.dp))
            )
        }

        Box(modifier = boxModifier.width(160.dp)) {
            Text(
                text = if(place.description.length > 20) place.description.substring(0, 20).replace('+', ' ') + "..." else place.description.replace('+', ' '),
                style = TextStyle(
                    fontSize = 16.sp
                )
            )
        }
        AttendanceForPlaces(
            selected = place.attendance
        )
        IconButton(
            onClick = placeOnMap,
        ){
            Icon(
                imageVector = Icons.Outlined.ShareLocation,
                contentDescription = "",
                tint = goldColor
            )
        }
    }
}

@Composable
fun AttendanceForPlaces(
    selected: Int
) {
    Row(
        modifier = Modifier
            .width(70.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        when (selected) {
            1 -> Image(
                painter = painterResource(id = R.drawable.person_maincolor_1),
                contentDescription = "",
                modifier = Modifier.size(20.dp)
            )
            2 -> Image(
                painter = painterResource(id = R.drawable.person_maincolor_2),
                contentDescription = "",
                modifier = Modifier.size(40.dp)
            )
            3 -> Image(
                painter = painterResource(id = R.drawable.person_maincolor_3),
                contentDescription = "",
                modifier = Modifier.height(20.dp)
            )
        }
    }
}

@Composable
fun PlaceWidget(
    index: Int,
    user: User,
    icon: Int,
    onClick: () -> Unit
){
    Column(
        modifier = Modifier
            .padding(10.dp)
            .clickable {
                onClick()
            },
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
        Image(
            painter = painterResource(id = icon),
            contentDescription = "${index}_place",
            modifier = Modifier.size(90.dp)
        )
        Spacer(modifier = Modifier.height(20.dp))
        Row {
            AsyncImage(
                model = user.image,
                contentDescription = "${index}_place",
                modifier = Modifier
                    .size(70.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = user.fullName.replace("+", " "))
        Text(
            text = user.score.toString() + "xp",
            color = Color.Black,
            style = TextStyle(fontSize = 10.sp)
        )
    }
}

@Composable
fun OtherPlacesWidget(
    users: List<User>,
    navController: NavController?
){
    val interactionSource = remember { MutableInteractionSource() }
    Spacer(modifier = Modifier.height(20.dp))
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                3.dp,
                lightYellow,
                shape = RoundedCornerShape(20.dp)
            )
    ) {
        users.forEachIndexed{
            index, user ->
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp, 20.dp, 20.dp, 20.dp)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {

                            val userJson = Gson().toJson(user)
                            val encodedUserJson =
                                URLEncoder.encode(userJson, StandardCharsets.UTF_8.toString())
                            navController?.navigate(Routes.userScreen + "/${encodedUserJson}")
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = (index + 3).toString(),
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    )
                    Spacer(modifier = Modifier.width(20.dp))
                    AsyncImage(
                        model = user.image,
                        contentDescription = "${index + 3}_place",
                        modifier = Modifier
                            .size(70.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = user.fullName.replace(",", " "))
                    Spacer(modifier = Modifier.width(40.dp))
                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = user.score.toString() + "xp",
                            color = Color.Black,
                            style = TextStyle(fontSize = 12.sp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SearchBar(
    inputValue: MutableState<String>,
    placesData: MutableList<Place>,
    navController: NavController,
    cameraPositionState: CameraPositionState
){
    val focusRequester = remember{
        FocusRequester()
    }
    val onFocus = remember {
        mutableStateOf(false)
    }

    val places = remember {
        mutableListOf<Place>()
    }

    places.clear()
    places.addAll(searchLogic(placesData, inputValue.value).toMutableList())

    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier.width(250.dp)
    ) {
        OutlinedTextField(
            modifier = Modifier
                .height(50.dp)
                .focusRequester(focusRequester = focusRequester)
                .onFocusChanged { focusState ->
                    onFocus.value = focusState.isFocused
                }
                .background(
                    Color.White,
                    shape = RoundedCornerShape(10.dp)
                )
                .border(
                    1.dp,
                    mainColor,
                    shape = RoundedCornerShape(10.dp)
                ),
            value = inputValue.value,
            onValueChange = { newValue ->
                inputValue.value = newValue
                onFocus.value = true
            },
            singleLine = true,
            placeholder = {
                Text(
                    text = "Unesite naziv mesta",
                    style = TextStyle(
                        color = greyTextColor
                    )
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Search,
                    contentDescription = "",
                    tint = mainColor
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
            ),
            visualTransformation = VisualTransformation.None,
            keyboardOptions = KeyboardOptions.Default
        )
        if(onFocus.value) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White),
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 300.dp)
                ) {
                    for (place in places) {
                        item {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 5.dp)
                                    .clickable {
                                        val placeJson = Gson().toJson(place)
                                        val encodedPlaceJson = URLEncoder.encode(
                                            placeJson,
                                            StandardCharsets.UTF_8.toString()
                                        )
                                        navController.navigate(Routes.placeScreen + "/$encodedPlaceJson")
                                    },
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    modifier = Modifier
                                        .padding(vertical = 8.dp)
                                        .weight(1f),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    AsyncImage(
                                        model = place.logo,
                                        contentDescription = "",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .width(40.dp)
                                            .height(40.dp)
                                            .clip(RoundedCornerShape(10.dp))
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(text = place.name)
                                }

                                IconButton(
                                    onClick = {
                                        onFocus.value = false
                                        keyboardController?.hide()
                                        cameraPositionState.position = CameraPosition.fromLatLngZoom(LatLng(place.location.latitude, place.location.longitude), 17f)
                                    },
                                    modifier = Modifier.wrapContentWidth()
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.MyLocation,
                                        contentDescription = "",
                                        tint = mainColor
                                    )
                                }
                            }
                        }
                    }
                }

            }
        }
    }
}

fun searchLogic(
    places: MutableList<Place>,
    inputValue: String
): List<Place>{
    val regex = inputValue.split(" ").joinToString(".*"){
        Regex.escape(it)
    }.toRegex(RegexOption.IGNORE_CASE)

    return places.filter { place ->
        regex.containsMatchIn(place.name)
    }
}

@Composable
fun PlaceMarker(
    place: Place,
    icon: BitmapDescriptor?,
    placesMarkers : MutableList<Place>,
    navController: NavController,
    notFiltered: Boolean
){
    Log.d("sta stize", place.location.toString())
    Marker(
        state = if(notFiltered){
            rememberMarkerState(
                position = LatLng(
                    place.location.latitude,
                    place.location.longitude
                ))
        }
        else{
            MarkerState(
                position = LatLng(
                    place.location.latitude,
                    place.location.longitude
                ))
        }
        ,
        title = place.name,
        icon = icon,
        snippet = place.description,
        onClick = {
            val placeJson = Gson().toJson(place)
            val encodedPlaceJson =
                URLEncoder.encode(
                    placeJson,
                    StandardCharsets.UTF_8.toString()
                )

            val placesJson = Gson().toJson(placesMarkers)
            val encodedPlacesJson = URLEncoder.encode(
                placesJson,
                StandardCharsets.UTF_8.toString()
            )
            navController.navigate(Routes.placeScreen + "/$encodedPlaceJson/$encodedPlacesJson")
            true
        }
    )
}