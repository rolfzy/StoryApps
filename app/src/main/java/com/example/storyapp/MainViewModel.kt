package com.example.storyapp


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.storyapp.api.ListStoryItem
import com.example.storyapp.data.UserModel
import kotlinx.coroutines.launch


class MainViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _allStories = MutableLiveData<PagingData<ListStoryItem>>()
    val allStories: LiveData<PagingData<ListStoryItem>> get() = _allStories

    init {
        getStories()

    }

//    val paging: LiveData<PagingData<ListStoryItem>> =
//        storyRepository.getPaging(token).cachedIn(viewModelScope)

    fun getAllStories(token: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val authToken = "Bearer $token"
                storyRepository.getPaging(authToken).cachedIn(viewModelScope).collect{ pagingData ->
                    _allStories.postValue(pagingData)
                }
            } catch (e: Exception) {
                // Handle the error
            } finally {
                _isLoading.value = false
            }
        }
    }

//    fun getAllStories(token: String) {
//        viewModelScope.launch {
//            try {
//                _isLoading.value = true
//                val apiService = ApiConfig.getApiService()
//                val successResponse = apiService.getStories("Bearer $token")
//                _stories.value = successResponse.listStory
//
//                Log.d(TAG, "MainViewModel success: ${successResponse.message}")
//            } catch (e: retrofit2.HttpException) {
//                val errorBody = e.response()?.errorBody()?.string()
//                val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
//
//                Log.d(TAG, "MainViewModel error: ${errorResponse.message}")
//            }
//            _isLoading.value = false
//        }
//    }


    fun getStories(): LiveData<UserModel> {
        return storyRepository.getStories().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            storyRepository.logout()
        }
    }

    companion object {
        private const val TAG = "MainViewModel"
    }


}

