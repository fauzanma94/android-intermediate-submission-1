package com.example.aplikasistoryapp.data.entity

import com.google.gson.annotations.SerializedName

data class UsersEntity (
    val userId: String,
    val name: String,
    val token: String,
    val _isLogin: Boolean

)
