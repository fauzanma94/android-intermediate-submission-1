package com.example.aplikasistoryapp.data

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.aplikasistoryapp.data.local.repository.AllStoryRepository
import com.example.aplikasistoryapp.ui.story.StoryViewModel

class StoryViewModelFactory (private val allStoryRepository: AllStoryRepository)
        : ViewModelProvider.NewInstanceFactory(){
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return when {
                        modelClass.isAssignableFrom(StoryViewModel::class.java) -> {
                                StoryViewModel(allStoryRepository) as T
                        }
                        else -> throw IllegalArgumentException("Unkown ViewModel class: " + modelClass.name)
                }
        }

        companion object {
                @Volatile
                private var instance: StoryViewModelFactory? = null

                fun getInstance(context: Context): StoryViewModelFactory =
                        instance ?: synchronized(this) {
                                instance ?: StoryViewModelFactory(Injection.setRepository(context))
                        }.also { instance = it }
        }
}