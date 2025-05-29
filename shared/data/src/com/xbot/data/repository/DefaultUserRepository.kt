package com.xbot.data.repository

import com.xbot.data.mapper.toDomain
import com.xbot.domain.models.Profile
import com.xbot.domain.repository.UserRepository
import com.xbot.network.client.auth.AuthTokenManager
import com.xbot.network.client.AnilibriaClient
import com.xbot.network.requests.account.getProfile
import com.xbot.network.requests.account.login
import com.xbot.network.requests.account.logout

internal class DefaultUserRepository(
    private val client: AnilibriaClient,
    private val accessTokenManager: AuthTokenManager,
) : UserRepository {
    override suspend fun login(
        login: String,
        password: String
    ): Result<Unit> = runCatching {
        val result = client.login(login, password)
        accessTokenManager.setAccessToken(result.token)
    }

    override suspend fun logout(): Result<Unit> = runCatching {
            val result = client.logout()
            accessTokenManager.setAccessToken(result.token)
    }

    override suspend fun getUserProfile(): Result<Profile> = runCatching {
        client.getProfile().toDomain()
    }
}