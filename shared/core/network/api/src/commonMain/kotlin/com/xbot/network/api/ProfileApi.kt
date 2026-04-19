package com.xbot.network.api

import arrow.core.Either
import com.xbot.domain.models.DomainError
import com.xbot.network.models.dto.ProfileDto

interface ProfileApi {
    suspend fun getProfile(): Either<DomainError, ProfileDto>
}
