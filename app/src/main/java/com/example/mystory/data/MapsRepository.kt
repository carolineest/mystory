package com.example.mystory.data

import com.example.mystory.data.api.response.ListStoryItem
import com.example.mystory.data.api.retrofit.ApiConfig
import com.example.mystory.data.api.retrofit.ApiService

class MapsRepository private constructor(private val apiService: ApiService) {

    suspend fun getStoriesWithLocation(token: String): List<ListStoryItem>? {
        val apiService = ApiConfig.getApiService(token)
        val storyResponse = apiService.getStoriesWithLocation()
        return storyResponse.listStory?.mapNotNull { it }
    }

    companion object {
        @Volatile
        private var instance: MapsRepository? = null

        fun getInstance(apiService: ApiService): MapsRepository = instance ?: synchronized(this) {
            instance ?: MapsRepository(apiService)
        }.also { instance = it }
    }
}
