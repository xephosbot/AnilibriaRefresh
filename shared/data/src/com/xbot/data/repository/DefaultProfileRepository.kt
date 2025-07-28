package com.xbot.data.repository

import com.xbot.data.mapper.toDomain
import com.xbot.domain.models.User
import com.xbot.domain.repository.ProfileRepository
import com.xbot.network.client.AnilibriaClient
import com.xbot.network.requests.account.getProfile

internal class DefaultProfileRepository(
    private val client: AnilibriaClient
) : ProfileRepository {
    override suspend fun getProfile(): Result<User> = runCatching {
        client.getProfile().toDomain()
    }

    override suspend fun updateProfile(user: User): Result<User> = runCatching {
        TODO("Not yet implemented")
    }
}