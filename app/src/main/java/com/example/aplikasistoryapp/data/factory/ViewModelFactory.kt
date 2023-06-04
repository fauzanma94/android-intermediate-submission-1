package com.example.aplikasistoryapp.data.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.aplikasistoryapp.data.local.store.Preference
import com.example.aplikasistoryapp.presentation.login.LoginViewModel
import com.example.aplikasistoryapp.presentation.story.UserViewModel

class ViewModelFactory (private val preference: Preference) : ViewModelProvider.NewInstanceFactory(){

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)){
            return LoginViewModel(preference) as T
        }
        if(modelClass.isAssignableFrom(UserViewModel::class.java)){
            return UserViewModel(preference) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }


}