package com.example.aplikasistoryapp.presentation.story

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.aplikasistoryapp.data.entity.UsersEntity
import com.example.aplikasistoryapp.data.local.store.Preference
import kotlinx.coroutines.launch

class UserViewModel(private val userPreference: Preference) : ViewModel() {

    fun getUser(): LiveData<UsersEntity> = userPreference.getUser().asLiveData()
    fun logout() {
        viewModelScope.launch { userPreference.userLogout() }
    }




}