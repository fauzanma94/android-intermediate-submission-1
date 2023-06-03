package com.example.aplikasistoryapp.ui.login

import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.aplikasistoryapp.R
import com.example.aplikasistoryapp.data.ViewModelFactory
import com.example.aplikasistoryapp.data.response.login.LoginResponse
import com.example.aplikasistoryapp.data.api.ApiConfig
import com.example.aplikasistoryapp.data.entity.UsersEntity
import com.example.aplikasistoryapp.data.local.store.Preference
import com.example.aplikasistoryapp.data.response.registration.RegistrationResponse
import com.example.aplikasistoryapp.databinding.FragmentLoginBinding
import com.example.aplikasistoryapp.ui.PasswordEditText
import com.google.android.material.tabs.TabLayout.TabGravity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var passwordEditText: PasswordEditText
    private lateinit var loginViewModel: LoginViewModel
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name="config")




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentLoginBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.registerAccount.setOnClickListener{
            findNavController().navigate(R.id.action_loginFragment_to_registrationFragment)
        }
        configurationViewModel(requireContext())
        binding.button.setOnClickListener{
            loginAuthenticationUser()
        }

        view.isFocusableInTouchMode = true
        view.requestFocus()
        view.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP) {
                requireActivity().finishAffinity()
                return@setOnKeyListener true
            }
            false
        }
    }

    private fun loginAuthenticationUser(){
        val email = binding.edLoginEmail.text.toString()
        val password = binding.edRegisterPassword.text.toString()
        val client = ApiConfig
            .getApiService()
            .postLogin(
                email,
                password
            )
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null){
                    loginViewModel.saveUser(
                        UsersEntity(
                            responseBody.loginResult.userId,
                            responseBody.loginResult.name,
                            responseBody.loginResult.token,
                            _isLogin = true
                        )
                    )
                    Toast.makeText(requireContext(),"Login Berhasil",Toast.LENGTH_SHORT).show()
                    Log.e(TAG, "onSuccess ${response.message()}")
                    findNavController().navigate(R.id.action_loginFragment_to_storyFragment)

                }else{
                    Toast.makeText(requireContext(),"User tidak ditemukan, Silahkan coba kembali",Toast.LENGTH_SHORT).show()
                    Log.e(TAG, "onFailure ${response.message()}")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toast.makeText(requireContext(),"User tidak ditemukan, Silahkan coba kembali",Toast.LENGTH_SHORT).show()
                Log.e(TAG, "onFailure ${t.message}")
            }
        })
    }

    private fun configurationViewModel(context: Context){
        val dataStore: DataStore<Preferences> = context.dataStore
        loginViewModel = ViewModelProvider(
            this,
            ViewModelFactory(
                Preference
                    .getInstance(dataStore))
        )[LoginViewModel::class.java]

    }



    companion object {

    }
}