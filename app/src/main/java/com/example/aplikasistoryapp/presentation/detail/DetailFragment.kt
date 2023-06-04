package com.example.aplikasistoryapp.presentation.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.aplikasistoryapp.R
import com.example.aplikasistoryapp.data.local.database.StoryUser
import com.example.aplikasistoryapp.databinding.FragmentDetailBinding

class DetailFragment : Fragment() {
    private lateinit var binding: FragmentDetailBinding
    private var storyName: String? = null
    private var storyDescription: String? = null
    private var storyPhotoUrl: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigate(R.id.action_detailFragment_to_storyFragment)
        }

        //Data ini diambil dari data yang ada pada menu storyFragment
        @Suppress("DEPRECATION")
        val data = arguments?.getParcelable<StoryUser>(EXTRA_DATA)
        if (data != null) {
            storyName = data.name
            storyDescription = data.description
            storyPhotoUrl = data.photoUrl

            binding.nameUser.text = storyName
            binding.description.text = storyDescription
            Glide.with(requireContext())
                .load(storyPhotoUrl)
                .into(binding.ivStory)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        const val EXTRA_DATA = "extra_data"
    }
}