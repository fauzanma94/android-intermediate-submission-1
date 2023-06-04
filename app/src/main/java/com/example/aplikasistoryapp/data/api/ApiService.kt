package com.example.aplikasistoryapp.data.api

import com.example.aplikasistoryapp.data.response.addstory.AddStoryResponse
import com.example.aplikasistoryapp.data.response.login.LoginResponse
import com.example.aplikasistoryapp.data.response.registration.RegistrationResponse
import com.example.aplikasistoryapp.data.response.story.AllStoriesResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query


interface ApiService {
    @FormUrlEncoded
    @POST("register")
    fun postRegistration(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<RegistrationResponse>

    @FormUrlEncoded
    @POST("login")
    fun postLogin(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @Multipart
    @POST("stories")
    fun addNewStory(
        @Header("Authorization") token: String,
        @Part("description") description: RequestBody,
        @Part file: MultipartBody.Part
    ): Call<AddStoryResponse>


    @GET("stories")
    fun getAllStories(
        @Header("Authorization") token: String,
        @Query("page") page: Int?,
        @Query("size") size: Int?
    ): Call<AllStoriesResponse>

}