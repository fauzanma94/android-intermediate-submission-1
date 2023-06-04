package com.example.aplikasistoryapp.presentation.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.aplikasistoryapp.R
import com.example.aplikasistoryapp.data.factory.ViewModelFactory
import com.example.aplikasistoryapp.data.api.ApiConfig
import com.example.aplikasistoryapp.data.entity.UsersEntity
import com.example.aplikasistoryapp.data.local.store.Preference
import com.example.aplikasistoryapp.data.response.login.LoginResponse
import com.example.aplikasistoryapp.databinding.FragmentLoginBinding
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var preference: Preference
    private lateinit var dataStore: DataStore<Preferences>
    private lateinit var loginViewModel: LoginViewModel
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "config")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.registerAccount.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registrationFragment)
        }
        configurationViewModel(requireContext())
        binding.button.setOnClickListener {
            loginAuthenticationUser()
        }
        setAnimation()

        view.isFocusableInTouchMode = true
        view.requestFocus()
        view.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP) {
                requireActivity().finishAffinity()
                return@setOnKeyListener true
            }
            false
        }

        dataStore = requireContext().dataStore
        preference = Preference.getInstance(dataStore)

        checkLoginStatus()
    }

    private fun setAnimation() {
        ObjectAnimator.ofFloat(binding.ivAccount, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val loginText = ObjectAnimator.ofFloat(binding.tvLogin, View.ALPHA, 1f).setDuration(500)
        val emailField =
            ObjectAnimator.ofFloat(binding.edLoginEmail, View.ALPHA, 1f).setDuration(500)
        val passwordField =
            ObjectAnimator.ofFloat(binding.edRegisterPassword, View.ALPHA, 1f).setDuration(500)
        val loginButton = ObjectAnimator.ofFloat(binding.button, View.ALPHA, 1f).setDuration(500)
        val registrationText =
            ObjectAnimator.ofFloat(binding.registerAccount, View.ALPHA, 1f).setDuration(500)
        val together = AnimatorSet().apply {
            playTogether(loginButton, registrationText)
        }

        AnimatorSet().apply {
            playSequentially(loginText, emailField, passwordField, together)
            start()
        }

    }

    private fun loginAuthenticationUser() {
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
                if (response.isSuccessful && responseBody != null) {
                    val user = UsersEntity(
                        responseBody.loginResult.userId,
                        responseBody.loginResult.name,
                        responseBody.loginResult.token,
                        _isLogin = true
                    )
                    loginViewModel.saveUser(user)
                    Toast.makeText(requireContext(), "Login Berhasil", Toast.LENGTH_SHORT).show()
                    Log.e(TAG, "onSuccess ${response.message()}")


                } else {
                    Toast.makeText(
                        requireContext(),
                        "User tidak ditemukan, Silahkan coba kembali",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e(TAG, "onFailure ${response.message()}")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toast.makeText(
                    requireContext(),
                    "User tidak ditemukan, Silahkan coba kembali",
                    Toast.LENGTH_SHORT
                ).show()
                Log.e(TAG, "onFailure ${t.message}")
            }
        })
    }


    private fun configurationViewModel(context: Context) {
        val dataStore: DataStore<Preferences> = context.dataStore
        loginViewModel = ViewModelProvider(
            this,
            ViewModelFactory(
                Preference
                    .getInstance(dataStore)
            )
        )[LoginViewModel::class.java]

    }

    private fun checkLoginStatus() {
        lifecycleScope.launch {
            preference.getUser().collect { user ->
                if (user._isLogin) {
                    findNavController().navigate(R.id.action_loginFragment_to_storyFragment)
                }
            }
        }

    }
}