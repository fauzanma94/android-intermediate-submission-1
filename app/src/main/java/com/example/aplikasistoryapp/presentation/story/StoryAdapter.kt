package com.example.aplikasistoryapp.presentation.story

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.aplikasistoryapp.R
import com.example.aplikasistoryapp.data.local.database.StoryUser
import com.example.aplikasistoryapp.databinding.ItemRowPostBinding
import com.example.aplikasistoryapp.presentation.detail.DetailFragment

class StoryAdapter : ListAdapter<StoryUser, StoryAdapter.ViewHolder>(DIFF_CALLBACK) {
    private var onItemClickCallback: ((StoryUser) -> Unit)? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRowPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val story = getItem(position)
        viewHolder.bind(story)

        viewHolder.itemView.setOnClickListener {
            onItemClickCallback?.invoke(story)
            val data = StoryUser(
                story.id,
                story.name,
                story.description,
                story.photoUrl,
                story.createdAt,
                story.latitude,
                story.longitude
            )

            val intent = Intent(it.context, DetailFragment::class.java)
            intent.putExtra(DetailFragment.EXTRA_DATA, data)

        }

        viewHolder.itemView.setOnClickListener {
            val data = getItem(position)
            val bundle = Bundle().apply {
                putParcelable(DetailFragment.EXTRA_DATA, data)
            }
            viewHolder.itemView.findNavController()
                .navigate(R.id.action_storyFragment_to_detailFragment, bundle)
        }

    }


    class ViewHolder(private val binding: ItemRowPostBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(storyUser: StoryUser) {

            binding.itemName.text = storyUser.name
            Glide.with(itemView.context)
                .load(storyUser.photoUrl)
                .apply(RequestOptions())
                .into(binding.itemPost)

        }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<StoryUser> =
            object : DiffUtil.ItemCallback<StoryUser>() {
                override fun areItemsTheSame(oldItem: StoryUser, newItem: StoryUser): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: StoryUser,
                    newItem: StoryUser
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}







