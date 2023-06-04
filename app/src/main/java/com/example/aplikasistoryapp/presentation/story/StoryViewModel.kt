package com.example.aplikasistoryapp.presentation.story

import androidx.lifecycle.ViewModel
import com.example.aplikasistoryapp.data.local.repository.AllStoryRepository

class StoryViewModel(private val allStoryRepository: AllStoryRepository) : ViewModel() {
    fun getAllStory(token: String) = allStoryRepository.getAllStory(token)


}