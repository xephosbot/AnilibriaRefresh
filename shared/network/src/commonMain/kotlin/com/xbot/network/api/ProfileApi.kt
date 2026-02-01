package com.xbot.network.api

import arrow.core.Either
import com.xbot.network.client.NetworkError
import com.xbot.network.client.request
import com.xbot.network.client.requiresAuth
import com.xbot.network.models.dto.ProfileDto
import io.ktor.client.*
import io.ktor.client.request.*

interface ProfileApi {
    suspend fun getProfile(): Either<NetworkError, ProfileDto>
}

internal class DefaultProfileApi(private val client: HttpClient) : ProfileApi {
    override suspend fun getProfile(): Either<NetworkError, ProfileDto> = client.request {
        get("accounts/users/me/profile") {
            requiresAuth()
        }
    }
}