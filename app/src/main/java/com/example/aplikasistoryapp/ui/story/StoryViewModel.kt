package com.example.aplikasistoryapp.ui.story

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.aplikasistoryapp.data.api.ApiConfig
import com.example.aplikasistoryapp.data.entity.UsersEntity
import com.example.aplikasistoryapp.data.local.repository.AllStoryRepository
import com.example.aplikasistoryapp.data.local.store.Preference
import com.example.aplikasistoryapp.data.response.story.AllStoriesResponse
import com.example.aplikasistoryapp.data.response.story.ListStoryResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class StoryViewModel(private val allStoryRepository: AllStoryRepository) : ViewModel() {
    fun getAllStory(token: String) = allStoryRepository.getAllStory(token)

}