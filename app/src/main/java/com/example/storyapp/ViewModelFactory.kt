package com.example.storyapp

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.storyapp.data.UserPreference
import com.example.storyapp.data.dataStore

import com.example.storyapp.Login.LoginViewModel
import com.example.storyapp.api.ApiConfig
import com.example.storyapp.maps.MapsViewModel
import com.example.storyapp.register.RegisterViewModel
import com.example.storyapp.upload.PostViewModel


class ViewModelFactory( private val storyRepository: StoryRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel() as T
            }
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(storyRepository) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(storyRepository) as T
            }
            modelClass.isAssignableFrom(PostViewModel::class.java) -> {
                PostViewModel(storyRepository) as T
            }
            modelClass.isAssignableFrom(MapsViewModel::class.java) -> {
                MapsViewModel(storyRepository ) as T
            }


            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory {
            return INSTANCE ?: synchronized(this) {
                val userPreference = UserPreference.getInstance(context.dataStore)
                val apiService = ApiConfig.getApiService()
                val repository = StoryRepository.getInstance(userPreference, apiService)
                ViewModelFactory(repository).also { INSTANCE = it }
            }
        }
    }
    }
