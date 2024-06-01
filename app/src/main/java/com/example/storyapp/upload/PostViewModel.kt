package com.example.storyapp.upload

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.storyapp.api.ApiConfig
import com.example.storyapp.api.ErrorResponse
import com.example.storyapp.data.UserModel
import com.example.storyapp.StoryRepository
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException

class PostViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading = _isLoading

    private val _token = MutableStateFlow("")
    val Token = _token


    init {
        getsesi()

    }


    fun getsesi(): LiveData<UserModel> {
        return storyRepository.getStories().asLiveData()
    }

    suspend fun uploadStory(
        token: String,
        fileImage: MultipartBody.Part,
        requestBody: RequestBody
    ) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val apiService = ApiConfig.getApiService()
                val response = apiService.uploadImage("Bearer $token", fileImage, requestBody)
                _token.value = response.message
                Log.d(TAG, "uploadImage sucess: ${response.message}")

            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
                _token.value = errorBody.message.toString()
                Log.d(TAG, "uploadImage Fail: ${errorBody.message}")

            } finally {
                _isLoading.value = false
            }

        }
    }


    companion object {
        const val TAG = "PostViewModel"
    }
}