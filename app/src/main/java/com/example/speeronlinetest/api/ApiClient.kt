package com.example.githubdemo.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object ApiClient {
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService: GitHubApiService by lazy {
        retrofit.create(GitHubApiService::class.java)
    }
}
