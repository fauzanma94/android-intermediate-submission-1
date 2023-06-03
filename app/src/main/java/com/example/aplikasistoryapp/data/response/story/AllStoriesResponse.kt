package com.example.aplikasistoryapp.data.response.story

import com.google.gson.annotations.SerializedName

data class AllStoriesResponse (
    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("listStory")
    val listStory: List<ListStoryResponse>,
    )