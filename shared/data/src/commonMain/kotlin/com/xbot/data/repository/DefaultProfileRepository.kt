package com.xbot.data.repository

import arrow.core.Either
import com.xbot.data.mapper.toDomain
import com.xbot.domain.models.Error
import com.xbot.domain.models.User
import com.xbot.domain.repository.ProfileRepository
import com.xbot.network.client.NetworkError
import com.xbot.network.api.ProfileApi

internal class DefaultProfileRepository(
    private val profileApi: ProfileApi
) : ProfileRepository {
    override suspend fun getProfile(): Either<Error, User> = profileApi
        .getProfile()
        .mapLeft(NetworkError::toDomain)
        .map(com.xbot.network.models.dto.ProfileDto::toDomain)

    override suspend fun updateProfile(user: User): Either<Error, User> = profileApi
        .getProfile()
        .mapLeft(NetworkError::toDomain)
        .map(com.xbot.network.models.dto.ProfileDto::toDomain)
}