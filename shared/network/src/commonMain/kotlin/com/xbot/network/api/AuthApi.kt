package com.xbot.network.api

import arrow.core.Either
import com.xbot.network.client.NetworkError
import com.xbot.network.client.request
import com.xbot.network.client.requiresAuth
import com.xbot.network.models.enums.SocialTypeDto
import com.xbot.network.models.responses.AuthResponse
import com.xbot.network.models.responses.LoginResponse
import com.xbot.network.models.responses.LogoutResponse
import com.xbot.network.models.responses.SocialAuthResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.koin.core.annotation.Singleton

interface AuthApi {
    suspend fun login(login: String, password: String): Either<NetworkError, LoginResponse>
    suspend fun logout(): Either<NetworkError, LogoutResponse>
    suspend fun socialLogin(provider: SocialTypeDto): Either<NetworkError, SocialAuthResponse>
    suspend fun socialAuthenticate(state: String): Either<NetworkError, AuthResponse>
    suspend fun forgotPassword(email: String): Either<NetworkError, Unit>
    suspend fun resetPassword(token: String, password: String, passwordConfirmation: String): Either<NetworkError, Unit>
}

@Singleton
internal class DefaultAuthApi(private val client: HttpClient) : AuthApi {
    override suspend fun login(
        login: String,
        password: String
    ): Either<NetworkError, LoginResponse> = client.request {
        post("accounts/users/auth/login") {
            contentType(ContentType.Application.Json)
            setBody(mapOf("login" to login, "password" to password))
        }
    }

    override suspend fun logout(): Either<NetworkError, LogoutResponse> = client.request {
        post("accounts/users/auth/logout") {
            requiresAuth()
        }
    }

    override suspend fun socialLogin(provider: SocialTypeDto): Either<NetworkError, SocialAuthResponse> = client.request {
        get("accounts/users/auth/social/$provider/login")
    }

    override suspend fun socialAuthenticate(state: String): Either<NetworkError, AuthResponse> = client.request {
        get("accounts/users/auth/social/authenticate") {
            parameter("state", state)
        }
    }

    override suspend fun forgotPassword(email: String): Either<NetworkError, Unit> = client.request {
        get("accounts/users/auth/password/forget") {
            contentType(ContentType.Application.Json)
            setBody(mapOf("email" to email))
        }
    }

    override suspend fun resetPassword(
        token: String,
        password: String,
        passwordConfirmation: String
    ): Either<NetworkError, Unit> = client.request {
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