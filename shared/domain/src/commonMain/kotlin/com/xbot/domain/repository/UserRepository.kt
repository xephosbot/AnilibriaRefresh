package com.xbot.domain.repository

import com.xbot.domain.models.Profile

interface UserRepository {
    suspend fun login(login: String, password: String): Result<Unit>
    suspend fun logout(): Result<Unit>
    suspend fun getUserProfile(): Result<Profile>
}