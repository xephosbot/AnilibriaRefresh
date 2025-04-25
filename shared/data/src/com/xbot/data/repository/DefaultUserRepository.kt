package com.xbot.data.repository

import com.xbot.api.client.AnilibriaClient
import com.xbot.api.client.SessionManager
import com.xbot.api.request.getUserProfile
import com.xbot.api.request.login
import com.xbot.api.request.logout
import com.xbot.data.mapper.toDomain
import com.xbot.domain.models.Profile
import com.xbot.domain.repository.UserRepository
import kotlinx.coroutines.flow.firstOrNull

internal class DefaultUserRepository(
    private val client: AnilibriaClient,
    private val sessionManager: SessionManager,
) : UserRepository {
    override suspend fun login(
        login: String,
        password: String
    ): Result<Unit> = runCatching {
        val result = client.login(login, password)
        sessionManager.setAccessToken(result.token)
    }

    override suspend fun logout(): Result<Unit> = runCatching {
            val result = client.logout()
            sessionManager.setAccessToken(result.token)
    }

    override suspend fun getUserProfile(): Result<Profile> = runCatching {
        client.getUserProfile().toDomain()
    }
}