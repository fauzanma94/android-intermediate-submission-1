package com.example.aplikasistoryapp.ui.story

import android.annotation.SuppressLint
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
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aplikasistoryapp.R
import com.example.aplikasistoryapp.data.StoryViewModelFactory
import com.example.aplikasistoryapp.data.ViewModelFactory
import com.example.aplikasistoryapp.data.local.repository.Result
import com.example.aplikasistoryapp.data.local.store.Preference
import com.example.aplikasistoryapp.data.response.story.ListStoryResponse
import com.example.aplikasistoryapp.databinding.FragmentStoryBinding
import com.example.aplikasistoryapp.ui.detail.DetailFragment
import com.example.aplikasistoryapp.ui.login.LoginViewModel
import kotlinx.coroutines.launch


class StoryFragment : Fragment() {
    private lateinit var binding: FragmentStoryBinding
    private lateinit var storyViewModel: StoryViewModel
    private lateinit var userViewModel: UserViewModel
    private lateinit var storyAdapter: StoryAdapter

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "config")


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStoryBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
//        storyAdapter.setOnItemClickCallback { story ->
//            val detailFragment = DetailFragment.newInstance(story)
//            findNavController().navigate(R.id.action_storyFragment_to_detailFragment, bundleOf(DetailFragment.EXTRA_DATA to story))
//            val bundle = Bundle().apply {
//                putParcelable(DetailFragment.EXTRA_DATA, story)
//            }
//            findNavController().navigate(R.id.action_storyFragment_to_detailFragment, bundle)
//        }

//        storyAdapter.setOnItemClickCallback { story ->
//            val detailFragment = DetailFragment.newInstance(story)
//            val bundle = Bundle().apply {
//                putParcelable("detailFragment", detailFragment)
//            }
//            findNavController().navigate(R.id.action_storyFragment_to_detailFragment, bundle)
//        }

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
                                    requireContext(),
                                    "Errorr Loading",
                                    Toast.LENGTH_SHORT
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




//        val adapter = StoryAdapter()
//        adapter.setOnItemClickCallback { story ->
//            val bundle = Bundle().apply {
//                putParcelable(DetailFragment.EXTRA_DATA, story)
//            }
//            val detailFragment = DetailFragment()
//            detailFragment.arguments = bundle
//
//            parentFragmentManager.beginTransaction()
//                .replace(R.id.container, detailFragment)
//                .addToBackStack(null)
//                .commit()
//        }

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
            Log.e(TAG, "onSuccess:")
            findNavController().navigate(R.id.action_storyFragment_to_loginFragment)

        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Gagal untuk Logout", Toast.LENGTH_SHORT).show()
            Log.e(TAG, "onFailure:")
            e.printStackTrace()
        }
    }


    private fun setupViewModel(context: Context) {
        val dataStore: DataStore<Preferences> = context.dataStore
        userViewModel = ViewModelProvider(
            this,
            ViewModelFactory(
                Preference
                    .getInstance(dataStore)
            )
        )[UserViewModel::class.java]
    }


}