package com.example.storyapp.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.storyapp.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding:ActivityDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getDetail()

    }

    private fun getDetail(){
        val photo =intent.getStringExtra(EXTRA_PHOTO)
        val name = intent.getStringExtra(EXTRA_NAME)
        val description = intent.getStringExtra(EXTRA_DESC)

        binding.apply {
            Glide.with(this@DetailActivity)
                .load(photo).into(binding.imgphoto)
            tvtitle.text =name
            tvdesc.text=description
        }

    }
    companion object{
        const val EXTRA_PHOTO ="extra_photourl"
        const val EXTRA_NAME ="extra_name"
        const val EXTRA_DESC ="extra_description"
    }
}