package com.example.storyapp.Login


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapp.api.ApiConfig
import com.example.storyapp.api.ApiService
import com.example.storyapp.data.UserModel
import com.example.storyapp.StoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

import kotlinx.coroutines.launch


class LoginViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    private val _token = MutableStateFlow<String?>(null)
    val token: StateFlow<String?> get() = _token
    private val apiService: ApiService
        get() = ApiConfig.getApiService()

    init {
        viewModelScope.launch {
            storyRepository.getStories().collect { user ->
                _token.value = user.token
            }
        }
    }

    fun login(email: String, password: String, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            try {
                val response = ApiConfig.getApiService().login(email,password)
                if (response.error == false) {
                    response.loginResult?.token?.let { token ->
                        saveSession(UserModel(email,token,true))
                    }
                    onResult(true, response.message ?: "Login Sukses")
                } else {
                    onResult(false, response.message ?: "Login Gagal")
                }
            } catch (e: Exception) {
                onResult(false, e.message ?: "Unknown Error")
            }
        }

    }
    fun isLoggedIn(): Flow<Boolean> {
        return storyRepository.isLoggedIn()
    }

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            storyRepository.saveStories(user)
        }
    }

    }




