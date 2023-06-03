package com.example.aplikasistoryapp.data

import android.content.Context
import com.example.aplikasistoryapp.data.api.ApiConfig
import com.example.aplikasistoryapp.data.local.database.StoryUserDatabase
import com.example.aplikasistoryapp.data.local.repository.AllStoryRepository
import com.example.aplikasistoryapp.data.local.repository.Executors

object Injection {
    fun setRepository(context: Context): AllStoryRepository{
        val apiService = ApiConfig.getApiService()
        val database = StoryUserDatabase.getDatabase(context)
        val dao = database.storyUserDao()
        val appExecutors = Executors()
        return AllStoryRepository.getInstance(apiService, dao, appExecutors)
    }
}