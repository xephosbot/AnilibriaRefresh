package com.xbot.shared.domain.repository

import com.xbot.shared.domain.models.Profile

interface UserRepository {
    suspend fun login(login: String, password: String): Result<Unit>
    suspend fun logout(): Result<Unit>
    suspend fun getUserProfile(): Result<Profile>
}