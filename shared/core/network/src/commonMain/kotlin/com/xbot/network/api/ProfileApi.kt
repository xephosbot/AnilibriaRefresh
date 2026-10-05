package com.xbot.network.api

import arrow.core.Either
import com.xbot.common.error.AppError
import com.xbot.network.models.dto.ProfileDto
import de.jensklingenberg.ktorfit.http.GET

interface ProfileApi {
    @GET("accounts/users/me/profile")
    suspend fun getProfile(): Either<AppError, ProfileDto>
}
