package com.example.storyapp


import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.storyapp.api.ApiConfig
import com.example.storyapp.api.ApiService
import com.example.storyapp.api.ListStoryItem
import com.example.storyapp.api.StoryResponse
import com.example.storyapp.data.UserModel
import com.example.storyapp.data.UserPreference
import com.example.storyapp.paging.PagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull


class StoryRepository private constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService

) {
    suspend fun saveStories(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun isLoggedIn(): Flow<Boolean> {
        return userPreference.isLoggedIn()
    }

    fun getStories(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    suspend fun getStoriesWithLocation(): StoryResponse {
        val token = userPreference.getToken().firstOrNull()
        return if (token != null) {
            apiService.getStoriesWithLocation("Bearer $token")
        } else {
            throw Exception("Token not found")
        }
    }
    fun getPaging(token: String): Flow<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { PagingSource(apiService,token) }
        ).flow

}

    suspend fun getStories(token: String, page: Int): StoryResponse {
        val apiServiceWithToken = ApiConfig.getApiService()
        return apiServiceWithToken.getStories(token)
    }

    companion object {
        @Volatile
        private var instance: StoryRepository? = null

        fun getInstance(userPreference: UserPreference, apiService: ApiService): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(userPreference, apiService).also { instance = it }
            }
    }
}