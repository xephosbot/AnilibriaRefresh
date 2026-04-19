package com.xbot.data.repository

import arrow.core.Either
import com.xbot.data.mapper.toDomain
import com.xbot.domain.models.DomainError
import com.xbot.domain.models.User
import com.xbot.network.api.ProfileApi
import org.koin.core.annotation.Singleton

@Singleton
internal class DefaultProfileRepository(
    private val profileApi: ProfileApi
) : ProfileRepository {
    override suspend fun getProfile(): Either<DomainError, User> = profileApi
        .getProfile()
        .map(com.xbot.network.models.dto.ProfileDto::toDomain)

    override suspend fun updateProfile(user: User): Either<DomainError, User> = profileApi
        .getProfile()
        .map(com.xbot.network.models.dto.ProfileDto::toDomain)
}