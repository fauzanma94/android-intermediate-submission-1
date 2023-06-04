package com.example.aplikasistoryapp.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplikasistoryapp.data.entity.UsersEntity
import com.example.aplikasistoryapp.data.local.store.Preference
import kotlinx.coroutines.launch

class LoginViewModel (private val preferences: Preference): ViewModel(){

    fun saveUser(user: UsersEntity){
        viewModelScope.launch {
            preferences.storeUser(user)
        }
    }

}