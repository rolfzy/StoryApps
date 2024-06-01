package com.example.storyapp.di


import android.content.Context

import com.example.storyapp.data.UserPreference
import com.example.storyapp.data.dataStore
import com.example.storyapp.StoryRepository
import com.example.storyapp.api.ApiService

//object Injection {
//    fun provideRepository(context: Context, apiService: ApiService): StoryRepository {
//        val pref = UserPreference.getInstance(context.dataStore)
//        return StoryRepository.getInstance(pref, apiService)
//    }
//}
