package com.example.storyapp.maps


import android.util.Log

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope

import com.example.storyapp.StoryRepository
import com.example.storyapp.api.ListStoryItem
import com.example.storyapp.data.UserModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MapsViewModel(
    private val storyRepository: StoryRepository,

) : ViewModel() {

    private val _storyLocation = MutableLiveData<List<ListStoryItem>>()
    val storyLocation: LiveData<List<ListStoryItem>> get() = _storyLocation

    fun getSession(): LiveData<UserModel?> {
        return storyRepository.getStories().asLiveData()
    }
    init {
        getStoryWithLocation()
    }

    private fun getStoryWithLocation() {
        viewModelScope.launch {
            try {

                val response = storyRepository.getStoriesWithLocation()
                _storyLocation.value = response.listStory
                if (response.error != true) {
                    Log.d("MapsViewModel", "Succes Add top Maps")
                } else {
                    Log.d("MapsViewModel", "No stories found near user location")
                }
            } catch (e: Exception) {
                Log.e("MapsViewModel", "Error fetching stories: ${e.message}")
            }
        }
    }



}