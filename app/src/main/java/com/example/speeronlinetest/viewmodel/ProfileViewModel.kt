package com.example.speeronlinetest.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.speeronlinetest.api.ApiException
import com.example.speeronlinetest.api.Resource
import com.example.speeronlinetest.data.Followers
import com.example.speeronlinetest.data.Profile
import com.example.speeronlinetest.repository.GitHubRepository
import kotlinx.coroutines.launch

class ProfileViewModel(private val repository: GitHubRepository) : ViewModel() {
    private val _profile = MutableLiveData<Profile?>()
    val profile: LiveData<Profile?> = _profile

    private val _selectedProfile = MutableLiveData<Profile?>()
    val selectedProfile: LiveData<Profile?> get() = _selectedProfile


    fun clearSelectedProfile() {
        _selectedProfile.value = null
    }

    fun resetProfile() {
        _profile.value = null
    }

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    val followersList: MutableLiveData<Resource<List<Followers>>> = MutableLiveData()
    val followingList: MutableLiveData<Resource<List<Followers>>> = MutableLiveData()



    fun fetchFollowersList() {
        followersList.value = Resource.loading()
        viewModelScope.launch {
            try {
                val followers = repository.getFollowersList(profile.value?.login ?: "")
                followersList.value = Resource.success(followers)
            } catch (e: ApiException) {
                followersList.value = Resource.error(e.message ?: "An error occurred")
            }
        }
    }

    fun fetchFollowingList() {
        followingList.value = Resource.loading()

        viewModelScope.launch {
            try {
                val following = repository.getFollowingList(profile.value?.login ?: "")
                followingList.value = Resource.success(following)
            } catch (e: ApiException) {
                followingList.value = Resource.error(e.message ?: "An error occurred")
            }
        }
    }


    fun fetchUserProfile(username: String) {
            viewModelScope.launch {
                try {
                    val userProfile = repository.getUserProfile(username)
                    _profile.value = userProfile
                } catch (e: Exception) {
                    _error.value = e.message
                }
            }
        }

    fun fetchSelectedUserProfile(username: String) {
        viewModelScope.launch {
            try {
                val userProfile = repository.getUserProfile(username)
                _selectedProfile.value = userProfile
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    }



