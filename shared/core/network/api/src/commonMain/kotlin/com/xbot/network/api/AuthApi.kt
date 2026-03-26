package com.xbot.network.api

import arrow.core.Either
import com.xbot.network.client.NetworkError
import com.xbot.network.models.enums.SocialTypeDto
import com.xbot.network.models.responses.AuthResponse
import com.xbot.network.models.responses.LoginResponse
import com.xbot.network.models.responses.LogoutResponse
import com.xbot.network.models.responses.SocialAuthResponse

interface AuthApi {
    suspend fun login(login: String, password: String): Either<NetworkError, LoginResponse>
    suspend fun logout(): Either<NetworkError, LogoutResponse>
    suspend fun socialLogin(provider: SocialTypeDto): Either<NetworkError, SocialAuthResponse>
    suspend fun socialAuthenticate(state: String): Either<NetworkError, AuthResponse>
    suspend fun forgotPassword(email: String): Either<NetworkError, Unit>
    suspend fun resetPassword(token: String, password: String, passwordConfirmation: String): Either<NetworkError, Unit>
}
