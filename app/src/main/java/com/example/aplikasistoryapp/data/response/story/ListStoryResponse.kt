package com.example.aplikasistoryapp.data.response.story

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ListStoryResponse (

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("photoUrl")
    val photoUrl: String,

    @field:SerializedName("description")
    val description: String,

    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("createdAt")
    val createdAt: String,

    @field:SerializedName("lat")
    val lat: Float,

    @field:SerializedName("lon")
    val lon: Float
): Parcelable