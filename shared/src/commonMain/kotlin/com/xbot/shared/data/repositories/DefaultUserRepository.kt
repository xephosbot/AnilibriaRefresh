package com.xbot.shared.data.repositories

import com.xbot.shared.data.mapper.toDomain
import com.xbot.shared.data.sources.local.TokenDataStore
import com.xbot.shared.data.sources.remote.AnilibriaClient
import com.xbot.shared.data.sources.remote.api.getUserProfile
import com.xbot.shared.data.sources.remote.api.login
import com.xbot.shared.data.sources.remote.api.logout
import com.xbot.shared.domain.models.Profile
import com.xbot.shared.domain.repository.UserRepository

internal class DefaultUserRepository(
    private val client: AnilibriaClient,
    private val tokenDataStore: TokenDataStore,
) : UserRepository {
    override suspend fun login(
        login: String,
        password: String
    ): Result<Unit> = runCatching {
        val result = client.login(login, password)
        if (result.token != null) {
            tokenDataStore.saveToken(result.token)
        } else {
            tokenDataStore.clearToken()
        }
    }

    override suspend fun logout(): Result<Unit> = runCatching {
        client.logout()
        tokenDataStore.clearToken()
    }

    override suspend fun getUserProfile(): Result<Profile> = runCatching {
        client.getUserProfile().toDomain()
    }
}