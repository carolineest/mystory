package com.example.mystory.ui.view.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mystory.data.UserRepository
import com.example.mystory.data.api.response.ErrorResponse
import com.example.mystory.data.api.response.RegisterResponse
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class SignupViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _registerResult = MutableLiveData<RegisterResponse>()
    val registerResult: LiveData<RegisterResponse> = _registerResult

    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            try {
                val response = userRepository.register(name, email, password)
                _registerResult.value = response
            } catch (e: HttpException) {
                val errorMessage = try {
                    val jsonInString = e.response()?.errorBody()?.string()
                    val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
                    errorBody.message ?: "An error occurred"
                } catch (e: Exception) {
                    e.message ?: "An error occurred"
                }
                _registerResult.value = RegisterResponse(
                    error = true,
                    message = errorMessage,
                )
            } catch (e: Exception) {
                _registerResult.value = RegisterResponse(
                    error = true,
                    message = e.message ?: "An error occurred",
                )
            }
        }
    }
}