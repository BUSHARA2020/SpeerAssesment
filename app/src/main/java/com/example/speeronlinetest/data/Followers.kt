package com.example.speeronlinetest.data

data class Followers(
    val avatar_url: String,
    val login: String
)

data class FollowersResponse(val followers: List<Followers>)

