package com.example.storyapp.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapp.api.ApiConfig
import com.example.storyapp.api.ErrorResponse
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class RegisterViewModel : ViewModel() {


    fun register(name:String,email: String, password:String,onResult: (Boolean,String) -> Unit){
         viewModelScope.launch {
        try {
            val apiService = ApiConfig.getApiService()
            val response = apiService.register(name, email, password)
            onResult(response.error == false,response.message ?: "unknow Error")
        } catch (e: HttpException) {
            //get error message
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody?.message ?: " Unknown error occurred"
            onResult(false,errorMessage)
        } catch (e:Exception){
            onResult(false,e.message ?: "Unknow Error")
        }
    }
}

}