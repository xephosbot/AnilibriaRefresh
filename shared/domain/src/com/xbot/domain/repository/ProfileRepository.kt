package com.xbot.domain.repository

import com.xbot.domain.models.User

interface ProfileRepository {
    suspend fun getProfile(): Result<User>
    suspend fun updateProfile(user: User): Result<User>
}