package com.example.githubdemo.api

import com.example.speeronlinetest.data.Followers
import com.example.speeronlinetest.data.Profile
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface GitHubApiService {
    @GET("users/{username}")
    suspend fun getUserProfile(@Path("username") username: String): Response<Profile>

    @GET("users/{username}/followers")
    suspend fun getFollowers(@Path("username") username: String): Response<List<Followers>>

    @GET("users/{username}/following")
    suspend fun getFollowing(@Path("username") username: String): Response<List<Followers>>



}


