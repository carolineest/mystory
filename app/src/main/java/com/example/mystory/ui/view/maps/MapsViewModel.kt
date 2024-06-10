package com.example.mystory.ui.view.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.mystory.data.MapsRepository
import com.example.mystory.data.UserRepository
import com.example.mystory.data.api.response.ListStoryItem
import com.example.mystory.data.pref.UserModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class MapsViewModel(
    private val mapsRepository: MapsRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    private val _storiesWithLocation = MutableLiveData<List<ListStoryItem>?>()
    val storiesWithLocation: LiveData<List<ListStoryItem>?> = _storiesWithLocation

    private val _loadingState = MutableLiveData<Boolean>()
    val loadingState: LiveData<Boolean> get() = _loadingState

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    fun fetchStoriesWithLocation(token: String) {
        viewModelScope.launch {
            try {
                val storyList = mapsRepository.getStoriesWithLocation(token)
                _storiesWithLocation.value = storyList
            } catch (e: HttpException) {
                if (e.code() == 401) {
                    _errorMessage.value = "Unauthorized access. Please log in again."
                } else {
                    _errorMessage.value = "An error occurred while fetching stories."
                }
            } catch (e: IOException) {
                _errorMessage.value = "Network error. Please check your internet connection."
            } finally {
                _loadingState.value = false
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
