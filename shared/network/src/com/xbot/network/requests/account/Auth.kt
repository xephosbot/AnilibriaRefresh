package com.xbot.network.requests.account

import com.xbot.network.client.AnilibriaClient
import com.xbot.network.models.enums.SocialTypeApi
import com.xbot.network.models.responses.accounts.auth.AuthResponse
import com.xbot.network.models.responses.accounts.auth.LoginResponse
import com.xbot.network.models.responses.accounts.auth.LogoutResponse
import com.xbot.network.models.responses.accounts.auth.SocialAuthResponse
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

suspend fun AnilibriaClient.login(
    login: String,
    password: String
): LoginResponse {
    val response: LoginResponse = request {
        post("accounts/users/auth/login") {
            contentType(ContentType.Application.Json)
            setBody(mapOf("login" to login, "password" to password))
        }
    }

    // Save token after successful login
    setAuthToken(response.token)

    return response
}

suspend fun AnilibriaClient.logout(): LogoutResponse {
    val response: LogoutResponse = request {
        post("accounts/users/auth/logout")
    }

    // Clear token after logout
    clearAuth()

    return response
}

suspend fun AnilibriaClient.socialLogin(
    provider: SocialTypeApi
): SocialAuthResponse = request {
    get("accounts/users/auth/social/$provider/login")
}

suspend fun AnilibriaClient.socialAuthenticate(
    state: String
): AuthResponse = request {
    get("accounts/users/auth/social/authenticate") {
        parameter("state", state)
    }
}

suspend fun AnilibriaClient.forgotPassword(
    email: String
): Unit = request {
    get("accounts/users/auth/password/forget") {
        contentType(ContentType.Application.Json)
        setBody(mapOf("email" to email))
    }
}

suspend fun AnilibriaClient.resetPassword(
    token: String,
    password: String,
    passwordConfirmation: String
): Unit = request {
    post("accounts/users/auth/password/reset") {
        contentType(ContentType.Application.Json)
        setBody(mapOf(
            "token" to token,
            "password" to password,
            "password_confirmation" to passwordConfirmation
        ))
    }
}