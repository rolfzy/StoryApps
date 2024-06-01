package com.example.storyapp

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingData
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

import com.example.storyapp.api.ListStoryItem
import com.example.storyapp.databinding.StoryLayoutBinding
import com.example.storyapp.detail.DetailActivity

class StoryAdapter : PagingDataAdapter<ListStoryItem, StoryAdapter.MyViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = StoryLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        }
    }


    class MyViewHolder(private val binding: StoryLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(story: ListStoryItem) {

            binding.tvstorytitle.text = story.name
            binding.tvstorydesc.text = story.description
            Glide.with(itemView.context)
                .load(story.photoUrl)
                .placeholder(R.drawable.baseline_broken_image_24)
                .error(R.drawable.baseline_broken_image_24)
                .into(binding.storyImage)

            binding.root.setOnClickListener {
                val context = binding.root.context
                val intent = Intent(context, DetailActivity::class.java).apply {
                    putExtra(DetailActivity.EXTRA_PHOTO, story.photoUrl)
                    putExtra(DetailActivity.EXTRA_NAME, story.name)
                    putExtra(DetailActivity.EXTRA_DESC, story.description)
                }
                context.startActivity(intent)
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem.id == newItem.id

            }

            override fun areContentsTheSame(
                oldItem: ListStoryItem,
                newItem: ListStoryItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}