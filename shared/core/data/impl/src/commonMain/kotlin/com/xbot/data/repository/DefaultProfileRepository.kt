package com.xbot.data.repository

import arrow.core.Either
import arrow.core.left
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

    /**
     * Not yet wired to a backend endpoint — [ProfileApi] exposes no update route.
     * Returning a typed [DomainError.UnknownError] surfaces "unimplemented" instead of
     * silently returning the unmodified profile (the previous behaviour was a
     * copy-paste of [getProfile] that ignored [user]). Replace with a real API call
     * once the backend contract is defined.
     */
    override suspend fun updateProfile(user: User): Either<DomainError, User> =
        DomainError.UnknownError(
            NotImplementedError("ProfileApi.updateProfile is not yet supported by the backend"),
        ).left()
}