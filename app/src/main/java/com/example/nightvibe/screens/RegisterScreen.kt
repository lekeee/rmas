package com.example.nightvibe.screens

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.MailOutline
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.nightvibe.navigation.Routes
import com.example.nightvibe.screens.components.GreyText
import com.example.nightvibe.screens.components.Heading1Text
import com.example.nightvibe.screens.components.LabelForInput
import com.example.nightvibe.screens.components.LoginRegisterButton
import com.example.nightvibe.screens.components.PasswordInput
import com.example.nightvibe.screens.components.TextInput
import com.example.nightvibe.screens.components.UploadIcon
import com.example.nightvibe.screens.components.customClickableText

@Composable
fun RegisterScreen(navController: NavController?){
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val fullName = remember { mutableStateOf("") }
    val phoneNumber = remember { mutableStateOf("") }
    val profileImage = remember { mutableStateOf(Uri.EMPTY) }

    val isEmailError = remember { mutableStateOf(false) }
    val emailErrorText = remember { mutableStateOf("") }

    val isPasswordError = remember { mutableStateOf(false) }
    val passwordErrorText = remember { mutableStateOf("") }

    val isImageError = remember { mutableStateOf(false) }
    val isFullNameError = remember { mutableStateOf(false) }
    val isPhoneNumberError = remember { mutableStateOf(false) }

    val isError = remember { mutableStateOf(false) }
    val errorText = remember { mutableStateOf("") }

    val buttonIsEnabled = remember { mutableStateOf(true) }
    val isLoading = remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(20.dp)
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 120.dp)
        ){
            Spacer(modifier = Modifier.height(40.dp))
            Heading1Text(textValue = "Nocni izlazak?")
            Spacer(modifier = Modifier.height(5.dp))
            GreyText(textValue = "Kreiraj nalog i kreni")
            Spacer(modifier = Modifier.height(20.dp))
            LabelForInput(textValue = "Vasa slika")
            UploadIcon()
            Spacer(modifier = Modifier.height(10.dp))
            LabelForInput(textValue = "Ime i Prezime")
            Spacer(modifier = Modifier.height(10.dp))
            TextInput(
                isEmail = false,
                inputValue = fullName,
                inputText = "Pera Peric",
                leadingIcon = Icons.Outlined.Person,
                isError = isFullNameError,
                errorText = emailErrorText
            )
            Spacer(modifier = Modifier.height(10.dp))
            LabelForInput(textValue = "Email adresa")
            Spacer(modifier = Modifier.height(10.dp))
            TextInput(
                isEmail = false,
                inputValue = email,
                inputText = "example@elfak.rs",
                leadingIcon = Icons.Outlined.MailOutline,
                isError = isEmailError,
                errorText = emailErrorText
            )
            Spacer(modifier = Modifier.height(10.dp))
            LabelForInput(textValue = "Broj telefona")
            Spacer(modifier = Modifier.height(10.dp))
            TextInput(
                isEmail = false,
                inputValue = phoneNumber,
                inputText = "063347567",
                leadingIcon = Icons.Outlined.Phone,
                isError = isPhoneNumberError,
                errorText = emailErrorText
            )
            Spacer(modifier = Modifier.height(10.dp))
            LabelForInput(textValue = "Lozinka")
            Spacer(modifier = Modifier.height(10.dp))
            PasswordInput(
                inputValue = password,
                inputText = "********",
                leadingIcon = Icons.Outlined.Lock,
                isError = isPasswordError,
                errorText = passwordErrorText
            )
            Spacer(modifier = Modifier.height(10.dp))
            LoginRegisterButton(
                buttonText = "Registrujte se",
                isEnabled = buttonIsEnabled,
                isLoading = isLoading) {
            }
            customClickableText(firstText = "Vec imate nalog? ", secondText = "Prijavi se", onClick = {
                navController?.navigate(Routes.loginScreen)
            })
        }
    }
}
