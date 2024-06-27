package com.example.nightvibe.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.nightvibe.viewmodels.AuthViewModel

class AuthViewModelFactory: ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(AuthViewModel::class.java)){
            return AuthViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}