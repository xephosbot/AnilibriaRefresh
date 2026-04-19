package com.xbot.network.api

import arrow.core.Either
import com.xbot.domain.models.DomainError
import com.xbot.network.client.ResilientHttpRequester
import com.xbot.network.client.requiresAuth
import com.xbot.network.models.dto.ProfileDto
import io.ktor.client.request.get
import org.koin.core.annotation.Singleton

@Singleton
internal class DefaultProfileApi(private val requester: ResilientHttpRequester) : ProfileApi {
    override suspend fun getProfile(): Either<DomainError, ProfileDto> = requester.request {
        get("accounts/users/me/profile") {
            requiresAuth()
        }
    }
}
