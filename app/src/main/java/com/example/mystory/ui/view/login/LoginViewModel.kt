package com.example.mystory.ui.view.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mystory.data.UserRepository
import com.example.mystory.data.api.response.ErrorResponse
import com.example.mystory.data.api.response.LoginResponse
import com.example.mystory.data.pref.UserModel
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class LoginViewModel(private val repository: UserRepository) : ViewModel() {

    private val _loginResult = MutableLiveData<LoginResponse>()
    val loginResult: LiveData<LoginResponse> = _loginResult

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                val response = repository.login(email, password)
                _loginResult.value = response
            } catch (e: HttpException) {
                val errorMessage = try {
                    val jsonInString = e.response()?.errorBody()?.string()
                    val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
                    errorBody.message ?: "An error occurred"
                } catch (e: Exception) {
                    e.message ?: "An error occurred"
                }
                _loginResult.value = LoginResponse(
                    error = true,
                    message = errorMessage,
                    loginResult = null
                )
            } catch (e: Exception) {
                _loginResult.value = LoginResponse(
                    error = true,
                    message = e.message ?: "An error occurred",
                    loginResult = null
                )
            }
        }
    }

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }
}