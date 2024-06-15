package com.example.nightvibe.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.nightvibe.screens.components.GreyText
import com.example.nightvibe.screens.components.Heading1Text
import com.example.nightvibe.screens.components.LabelForInput
import com.example.nightvibe.screens.components.LoginRegisterButton
import com.example.nightvibe.screens.components.NightOutImage
import com.example.nightvibe.screens.components.PasswordInput
import com.example.nightvibe.screens.components.TextInput

@Composable
fun LoginScreen(navController: NavController?){

    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }

    val isEmailError = remember { mutableStateOf(false) }
    val emailErrorText = remember { mutableStateOf("") }

    val isPasswordError = remember { mutableStateOf(false) }
    val passwordErrorText = remember { mutableStateOf("") }

    val isError = remember { mutableStateOf(false) }
    val errorText = remember { mutableStateOf("") }

    val buttonIsEnabled = remember { mutableStateOf(true) }
    val isLoading = remember { mutableStateOf(false) }

    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color.White)
        .padding(20.dp)) {
        NightOutImage()
        Heading1Text(textValue = "Dobrodosli")
        Spacer(modifier = Modifier.height(5.dp))
        GreyText(textValue = "Drago nam je sto ste ovde!")
        Spacer(modifier = Modifier.height(20.dp))
        LabelForInput(textValue = "Unesite email")
        Spacer(modifier = Modifier.height(10.dp))
        TextInput(
            isEmail = true,
            inputValue = email,
            inputText = "example@elfak.rs",
            leadingIcon = Icons.Outlined.Email,
            isError = isEmailError,
            errorText = emailErrorText
        )
        Spacer(modifier = Modifier.height(20.dp))
        LabelForInput(textValue = "Unesite lozinku")
        Spacer(modifier = Modifier.height(10.dp))
        PasswordInput(
            inputValue = password,
            inputText = "********",
            leadingIcon = Icons.Outlined.Lock,
            isError = isPasswordError,
            errorText = passwordErrorText
        )
        Spacer(modifier = Modifier.height(50.dp))
        LoginRegisterButton(
            buttonText = "Prijavi se",
            isEnabled = buttonIsEnabled,
            isLoading = isLoading) {
        }
    }
}

@Preview
@Composable
fun LoginScreenPreview(){
    LoginScreen(null)
}
