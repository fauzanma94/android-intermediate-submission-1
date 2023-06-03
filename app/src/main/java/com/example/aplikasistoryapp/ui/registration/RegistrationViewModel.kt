package com.example.aplikasistoryapp.ui.registration

import android.app.AlertDialog
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.example.aplikasistoryapp.data.api.ApiConfig
import com.example.aplikasistoryapp.data.response.registration.RegistrationResponse
import com.example.aplikasistoryapp.databinding.FragmentRegistrationBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegistrationViewModel: ViewModel() {
    private lateinit var binding: FragmentRegistrationBinding

    fun registerUser(){
        val name = binding.edRegisterName.text.toString()
        val email = binding.edRegisterEmail.text.toString()
        val password = binding.edRegisterPassword.text.toString()

        val client = ApiConfig
            .getApiService()
            .postRegistration(
                name,
                email,
                password
            )
        client.enqueue(object : Callback<RegistrationResponse>{
            override fun onResponse(
                call: Call<RegistrationResponse>,
                response: Response<RegistrationResponse>
            ) {
                val responseBody = response.body()
                if(response.isSuccessful && responseBody != null){
                    Log.e(TAG, "onSuccess: ${response.message()}")
                } else{
                    Log.e(TAG, "onFailure ${response.message()}")
                }
            }

            override fun onFailure(call: Call<RegistrationResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })

    }
}