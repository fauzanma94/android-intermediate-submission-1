package com.example.aplikasistoryapp.presentation.story

import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aplikasistoryapp.R
import com.example.aplikasistoryapp.data.factory.StoryViewModelFactory
import com.example.aplikasistoryapp.data.factory.ViewModelFactory
import com.example.aplikasistoryapp.data.local.repository.Result
import com.example.aplikasistoryapp.data.local.store.Preference
import com.example.aplikasistoryapp.databinding.FragmentStoryBinding


class StoryFragment : Fragment() {
    private lateinit var binding: FragmentStoryBinding
    private lateinit var userViewModel: UserViewModel
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "config")


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentStoryBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        @Suppress("DEPRECATION")
        setHasOptionsMenu(true)


        val toolbar = view.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        binding.addStory.setOnClickListener {
            findNavController().navigate(R.id.action_storyFragment_to_addStoryFragment)
        }



        setupViewModel(requireContext())

        val storyViewModelFactory: StoryViewModelFactory =
            StoryViewModelFactory.getInstance(requireActivity())
        val storyViewModel: StoryViewModel by viewModels { storyViewModelFactory }
        val storyAdapter = StoryAdapter()

        userViewModel.getUser().observe(viewLifecycleOwner) { user ->
            if (user._isLogin) {
                storyViewModel.getAllStory("Bearer ${user.token}")
                    .observe(viewLifecycleOwner) { result ->
                        when (result) {
                            is Result.Success -> {
                                val story = result.data
                                storyAdapter.submitList(story)
                            }

                            is Result.Error -> {
                                Toast.makeText(
                                    requireContext(), "Errorr Loading", Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
            }
        }



        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = storyAdapter
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

    @Suppress("DEPRECATION")
    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.option_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


    @Suppress("DEPRECATION")
    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logout -> {
                logout()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun logout() {
        try {
            userViewModel.logout()
            Toast.makeText(requireContext(), "Berhasil Logout", Toast.LENGTH_SHORT).show()
            Log.e(TAG, "onSuccess: Logout Success")
            findNavController().navigate(R.id.action_storyFragment_to_loginFragment)

        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Gagal untuk Logout", Toast.LENGTH_SHORT).show()
            Log.e(TAG, "onFailure: Logout Failed")
            e.printStackTrace()
        }
    }


    private fun setupViewModel(context: Context) {
        val dataStore: DataStore<Preferences> = context.dataStore
        userViewModel = ViewModelProvider(
            this, ViewModelFactory(
                Preference.getInstance(dataStore)
            )
        )[UserViewModel::class.java]
    }




}