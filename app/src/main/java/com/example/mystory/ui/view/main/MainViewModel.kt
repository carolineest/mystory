package com.example.mystory.ui.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.mystory.data.UserRepository
import com.example.mystory.data.StoryRepository
import com.example.mystory.data.api.response.ListStoryItem
import com.example.mystory.data.pref.UserModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class MainViewModel(
    private val userRepository: UserRepository,
    private val storyRepository: StoryRepository
) : ViewModel() {
    private val _stories = MutableLiveData<PagingData<ListStoryItem>>()
    val stories: LiveData<PagingData<ListStoryItem>> get() = _stories

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    private val _loadingState = MutableLiveData<Boolean>()
    val loadingState: LiveData<Boolean> = _loadingState

    fun fetchStories(token: String) {
        viewModelScope.launch {
            _loadingState.value = true
            try {
                storyRepository.getStories(token)
                    .cachedIn(viewModelScope)
                    .collectLatest { pagingData ->
                        _stories.postValue(pagingData)
                        _loadingState.value = false
                    }
            } catch (e: HttpException) {
                if (e.code() == 401) {
                    _errorMessage.value = "Unauthorized access. Please log in again."
                } else {
                    _errorMessage.value = "An error occurred while fetching stories."
                }
            } catch (e: IOException) {
                _errorMessage.value = "Network error. Please check your internet connection."
            }
        }
    }

    fun getSession(): LiveData<UserModel> {
        return userRepository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            userRepository.logout()
        }
    }
}
