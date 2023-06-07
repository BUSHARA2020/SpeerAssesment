package com.example.speeronlinetest.data

data class Profile(
    val avatar_url: String,
    val login: String,
    val name: String,
    val bio: String,
    val followers: Int,
    val following: Int
)
