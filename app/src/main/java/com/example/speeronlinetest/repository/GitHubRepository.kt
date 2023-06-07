package com.example.speeronlinetest.repository

import com.example.speeronlinetest.data.Followers
import com.example.speeronlinetest.data.Profile

interface GitHubRepository {
    suspend fun getUserProfile(username: String): Profile
    suspend fun getFollowersList(username: String): List<Followers>
    suspend fun getFollowingList(username: String): List<Followers>


}
