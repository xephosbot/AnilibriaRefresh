package com.xbot.network.api

import arrow.core.Either
import com.xbot.common.error.AppError
import com.xbot.network.models.dto.ProfileDto

interface ProfileApi {
    suspend fun getProfile(): Either<AppError, ProfileDto>
}
