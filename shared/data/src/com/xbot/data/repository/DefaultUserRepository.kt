package com.xbot.data.repository

import com.xbot.data.mapper.toDomain
import com.xbot.domain.models.Profile
import com.xbot.domain.repository.UserRepository
import com.xbot.network.client.AnilibriaClient
import com.xbot.network.requests.account.getProfile
import com.xbot.network.requests.account.login
import com.xbot.network.requests.account.logout

internal class DefaultUserRepository(
    private val client: AnilibriaClient,
) : UserRepository {
    override suspend fun login(
        login: String,
        password: String
    ): Result<Unit> = runCatching {
        client.login(login, password)
    }

    override suspend fun logout(): Result<Unit> = runCatching {
        client.logout()
    }

    override suspend fun getUserProfile(): Result<Profile> = runCatching {
        client.getProfile().toDomain()
    }
}