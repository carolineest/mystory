package com.example.mystory.data

import com.example.mystory.data.api.response.LoginResponse
import com.example.mystory.data.api.response.RegisterResponse
import com.example.mystory.data.api.retrofit.ApiService
import com.example.mystory.data.pref.UserModel
import com.example.mystory.data.pref.UserPreference
import kotlinx.coroutines.flow.Flow

class UserRepository private constructor(
    private val apiService: ApiService,
    private val userPreference: UserPreference
) {

    suspend fun register(name: String, email: String, password: String): RegisterResponse {
        return apiService.register(name, email, password)
    }

    suspend fun login(email: String, password: String): LoginResponse {
        val response = apiService.login(email, password)
        if (!response.error) {
            response.loginResult?.let {
                val userModel = UserModel(
                    email = email,
                    token = it.token,
                    isLogin = true
                )
                saveSession(userModel)
            }
        }
        return response
    }

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            apiService: ApiService,
            userPreference: UserPreference
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService, userPreference)
            }.also { instance = it }
    }
}