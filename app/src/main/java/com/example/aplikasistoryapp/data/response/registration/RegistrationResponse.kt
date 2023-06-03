package com.example.aplikasistoryapp.data.response.registration

import com.google.gson.annotations.SerializedName

data class RegistrationResponse (
    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String
)