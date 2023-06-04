package com.example.aplikasistoryapp.presentation.registration

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.aplikasistoryapp.R
import com.example.aplikasistoryapp.data.api.ApiConfig
import com.example.aplikasistoryapp.data.response.registration.RegistrationResponse
import com.example.aplikasistoryapp.databinding.FragmentRegistrationBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RegistrationFragment : Fragment() {
    private lateinit var binding: FragmentRegistrationBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvLogin.setOnClickListener {
            findNavController().navigate(R.id.action_registrationFragment_to_loginFragment)
        }
        binding.button.setOnClickListener {
            registerUser()
        }

        setAnimation()
    }

    private fun setAnimation() {
        ObjectAnimator.ofFloat(binding.ivAccount, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val registrationText =
            ObjectAnimator.ofFloat(binding.tvRegistration, View.ALPHA, 1f).setDuration(500)
        val registrationTv =
            ObjectAnimator.ofFloat(binding.tvDescRegistration, View.ALPHA, 1f).setDuration(500)
        val nameField =
            ObjectAnimator.ofFloat(binding.edRegisterName, View.ALPHA, 1f).setDuration(500)
        val emailField =
            ObjectAnimator.ofFloat(binding.edRegisterEmail, View.ALPHA, 1f).setDuration(500)
        val passwordField =
            ObjectAnimator.ofFloat(binding.edRegisterPassword, View.ALPHA, 1f).setDuration(500)
        val loginButton = ObjectAnimator.ofFloat(binding.button, View.ALPHA, 1f).setDuration(500)
        val loginText = ObjectAnimator.ofFloat(binding.tvLogin, View.ALPHA, 1f).setDuration(500)
        val together = AnimatorSet().apply {
            playTogether(loginButton, loginText)
        }

        AnimatorSet().apply {
            playSequentially(
                registrationText,
                registrationTv,
                nameField,
                emailField,
                passwordField,
                together
            )
            start()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun registerUser() {
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
                if (response.isSuccessful && responseBody != null) {
                    Log.e(ContentValues.TAG, "onSuccess: ${response.message()}")
                    Toast.makeText(
                        requireContext(),
                        "Akun Berhasil Dibuat, silahkan login",
                        Toast.LENGTH_SHORT
                    ).show()
                    findNavController().navigate(R.id.action_registrationFragment_to_loginFragment)
                } else {
                    Log.e(ContentValues.TAG, "onFailure ${response.message()}")
                }
            }

            override fun onFailure(call: Call<RegistrationResponse>, t: Throwable) {
                Log.e(ContentValues.TAG, "onFailure: ${t.message.toString()}")
            }
        })

    }

}