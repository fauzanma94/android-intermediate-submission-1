package com.example.aplikasistoryapp.data

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.aplikasistoryapp.data.local.store.Preference
import com.example.aplikasistoryapp.ui.login.LoginViewModel
import com.example.aplikasistoryapp.ui.story.StoryViewModel
import com.example.aplikasistoryapp.ui.story.UserViewModel

class ViewModelFactory (private val preference: Preference) : ViewModelProvider.NewInstanceFactory(){
    companion object{
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(preference: Preference): ViewModelFactory{
            if (INSTANCE == null){
                synchronized(ViewModelFactory::class.java){
                    INSTANCE = ViewModelFactory(preference)
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }

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