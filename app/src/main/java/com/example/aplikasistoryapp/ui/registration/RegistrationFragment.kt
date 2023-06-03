package com.example.aplikasistoryapp.ui.registration

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.example.aplikasistoryapp.R
import com.example.aplikasistoryapp.data.api.ApiConfig
import com.example.aplikasistoryapp.data.response.registration.RegistrationResponse
import com.example.aplikasistoryapp.databinding.FragmentRegistrationBinding
import com.example.aplikasistoryapp.ui.PasswordEditText
import com.example.aplikasistoryapp.ui.login.LoginViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RegistrationFragment : Fragment() {
    private lateinit var binding: FragmentRegistrationBinding
    private lateinit var passwordEditText: PasswordEditText
    private lateinit var registrationViewModel: RegistrationViewModel


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.button.setOnClickListener {
            registerUser()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentRegistrationBinding.inflate(inflater,container,false)
        return binding.root
    }

    private fun registerUser(){
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
        client.enqueue(object : Callback<RegistrationResponse> {
            override fun onResponse(
                call: Call<RegistrationResponse>,
                response: Response<RegistrationResponse>
            ) {
                val responseBody = response.body()
                if(response.isSuccessful && responseBody != null){
                    Log.e(ContentValues.TAG, "onSuccess: ${response.message()}")
                    Toast.makeText(requireContext(),"Akun Berhasil Dibuat, silahkan login",Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_registrationFragment_to_loginFragment)
                } else{
                    Log.e(ContentValues.TAG, "onFailure ${response.message()}")
                }
            }

            override fun onFailure(call: Call<RegistrationResponse>, t: Throwable) {
                Log.e(ContentValues.TAG, "onFailure: ${t.message.toString()}")
            }
        })

    }

    companion object {

    }
}