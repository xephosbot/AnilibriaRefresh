package com.xbot.network.api

import arrow.core.Either
import com.xbot.common.error.AppError
import com.xbot.network.client.HttpRequester
import com.xbot.network.client.requiresAuth
import com.xbot.network.models.enums.SocialTypeDto
import com.xbot.network.models.responses.AuthResponse
import com.xbot.network.models.responses.LoginResponse
import com.xbot.network.models.responses.LogoutResponse
import com.xbot.network.models.responses.SocialAuthResponse
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.koin.core.annotation.Singleton

@Singleton
internal class DefaultAuthApi(private val requester: HttpRequester) : AuthApi {
    override suspend fun login(
        login: String,
        password: String
    ): Either<AppError, LoginResponse> = requester.request {
        post("accounts/users/auth/login") {
            contentType(ContentType.Application.Json)
            setBody(mapOf("login" to login, "password" to password))
        }
    }

    override suspend fun logout(): Either<AppError, LogoutResponse> = requester.request {
        post("accounts/users/auth/logout") {
            requiresAuth()
        }
    }

    override suspend fun socialLogin(provider: SocialTypeDto): Either<AppError, SocialAuthResponse> = requester.request {
        get("accounts/users/auth/social/$provider/login")
    }

    override suspend fun socialAuthenticate(state: String): Either<AppError, AuthResponse> = requester.request {
        get("accounts/users/auth/social/authenticate") {
            parameter("state", state)
        }
    }

    override suspend fun forgotPassword(email: String): Either<AppError, Unit> = requester.request {
        get("accounts/users/auth/password/forget") {
            contentType(ContentType.Application.Json)
            setBody(mapOf("email" to email))
        }
    }

    override suspend fun resetPassword(
        token: String,
        password: String,
        passwordConfirmation: String
    ): Either<AppError, Unit> = requester.request {
        post("accounts/users/auth/password/reset") {
            contentType(ContentType.Application.Json)
            setBody(
                mapOf(
                    "token" to token,
                    "password" to password,
                    "password_confirmation" to passwordConfirmation
                )
            )
        }
    }
}
