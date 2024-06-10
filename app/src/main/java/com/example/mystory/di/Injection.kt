package com.example.mystory.di

import android.content.Context
import com.example.mystory.data.MapsRepository
import com.example.mystory.data.UserRepository
import com.example.mystory.data.StoryRepository
import com.example.mystory.data.api.retrofit.ApiConfig
import com.example.mystory.data.pref.UserPreference
import com.example.mystory.data.pref.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideUserRepository(context: Context): UserRepository {
        val apiService = ApiConfig.getApiService()
        val pref = UserPreference.getInstance(context.dataStore)
        return UserRepository.getInstance(apiService, pref)
    }

    fun provideStoryRepository(context: Context): StoryRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token)
        return StoryRepository.getInstance(apiService, pref)
    }

    fun provideMapsRepository(context: Context): MapsRepository {
        val apiService = ApiConfig.getApiService()
        return MapsRepository.getInstance(apiService)
    }
}