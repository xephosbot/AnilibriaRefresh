package com.xbot.network.api

import arrow.core.Either
import com.xbot.network.client.NetworkError
import com.xbot.network.models.dto.ProfileDto

interface ProfileApi {
    suspend fun getProfile(): Either<NetworkError, ProfileDto>
}
