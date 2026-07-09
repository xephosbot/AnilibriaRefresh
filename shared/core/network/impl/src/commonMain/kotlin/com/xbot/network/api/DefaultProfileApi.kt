package com.xbot.network.api

import arrow.core.Either
import com.xbot.common.error.AppError
import com.xbot.network.client.HttpRequester
import com.xbot.network.client.requiresAuth
import com.xbot.network.models.dto.ProfileDto
import io.ktor.client.request.get
import org.koin.core.annotation.Singleton

@Singleton
internal class DefaultProfileApi(private val requester: HttpRequester) : ProfileApi {
    override suspend fun getProfile(): Either<AppError, ProfileDto> = requester.request {
        get("accounts/users/me/profile") {
            requiresAuth()
        }
    }
}
