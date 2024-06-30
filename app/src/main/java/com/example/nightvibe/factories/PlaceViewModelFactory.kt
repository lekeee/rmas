package com.example.nightvibe.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.nightvibe.viewmodels.PlaceViewModel

class PlaceViewModelFactory: ViewModelProvider.Factory{
    override fun <T: ViewModel> create(modelClass: Class<T>): T{
        if(modelClass.isAssignableFrom(PlaceViewModel::class.java)){
            return PlaceViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}