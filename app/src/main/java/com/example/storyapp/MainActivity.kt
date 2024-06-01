package com.example.storyapp

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storyapp.Login.LoginActivity
import com.example.storyapp.databinding.ActivityMainBinding
import com.example.storyapp.maps.MapsActivity
import com.example.storyapp.paging.LoadingAdapter
import com.example.storyapp.upload.PostActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var storyAdapter: StoryAdapter

    private var token = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getStories().observe(this) { user ->
            runBlocking { delay(1500) }
            if (user.token.isNotEmpty() && user.token != "") {
                token = user.token
                setupWindow()
                setupTopBar(token)
                setupRecyclerView()
                viewModel.getAllStories(token) // Load stories with token
                Log.d("TAG", "Token: $token")
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }

        binding.postStory.setOnClickListener {
            startActivity(Intent(this@MainActivity, PostActivity::class.java))
        }
        binding.maps.setOnClickListener {
            startActivity(Intent(this@MainActivity, MapsActivity::class.java))
        }
    }

    private fun setupTopBar(token: String) {
        binding.topAppBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.Exitapp -> {
                    viewModel.logout()
                    true
                }
                else -> false
            }
        }
    }

    private fun setupRecyclerView() {
        storyAdapter = StoryAdapter()
        binding.rvMain.adapter = storyAdapter.withLoadStateFooter(
            footer = LoadingAdapter {
                storyAdapter.retry()
            }
        )
        binding.rvMain.layoutManager = LinearLayoutManager(this)

        viewModel.allStories.observe(this) { pagingData ->
            storyAdapter.submitData(lifecycle, pagingData)
        }
    }

    private fun setupWindow() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()

        viewModel.isLoading.observe(this@MainActivity) {
            showLoading(it)
        }
    }

    private fun showLoading(loading: Boolean) {
        binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
    }
}
