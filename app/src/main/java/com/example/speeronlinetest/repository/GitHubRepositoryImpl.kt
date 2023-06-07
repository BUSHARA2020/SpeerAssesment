package com.example.speeronlinetest.repository

import android.util.Log
import com.example.githubdemo.api.ApiClient
import com.example.githubdemo.api.GitHubApiService
import com.example.speeronlinetest.api.ApiException
import com.example.speeronlinetest.data.Followers
import com.example.speeronlinetest.data.Profile

class GitHubRepositoryImpl(private val apiService: GitHubApiService) : GitHubRepository {
    override suspend fun getUserProfile(username: String): Profile {
        // Make the API request using Retrofit and return the user profile
        val response = apiService.getUserProfile(username)
        if (response.isSuccessful) {

            return response.body() ?: throw NullPointerException("User profile is null")
        } else {
            throw ApiException(response.code(), response.message())
        }
    }

    override suspend fun getFollowersList(username: String): List<Followers> {
        // Make the network request to fetch the followers list using the GitHub API
        val response = apiService.getFollowers(username)

        if (response.isSuccessful) {
            return (response.body() ?: emptyList<Followers>())
        } else {
            // Handle the error case
            throw ApiException(response.code(), response.message())
        }
    }

    override suspend fun getFollowingList(username: String): List<Followers> {
        // Make the network request to fetch the followings list using the GitHub API
        val response = ApiClient.apiService.getFollowing(username)
        return if (response.isSuccessful) {
            (response.body() ?: emptyList<Followers>())
        } else {
            // Handle the error case
            throw ApiException(response.code(), response.message())
        }
    }
}



