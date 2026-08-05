package com.xbot.network.api

import arrow.core.Either
import com.xbot.common.error.AppError
import com.xbot.network.models.enums.SocialTypeDto
import com.xbot.network.models.responses.AuthResponse
import com.xbot.network.models.responses.LoginResponse
import com.xbot.network.models.responses.LogoutResponse
import com.xbot.network.models.responses.SocialAuthResponse

interface AuthApi {
    suspend fun login(login: String, password: String): Either<AppError, LoginResponse>
    suspend fun logout(): Either<AppError, LogoutResponse>
    suspend fun socialLogin(provider: SocialTypeDto): Either<AppError, SocialAuthResponse>
    suspend fun socialAuthenticate(state: String): Either<AppError, AuthResponse>
    suspend fun forgotPassword(email: String): Either<AppError, Unit>
    suspend fun resetPassword(token: String, password: String, passwordConfirmation: String): Either<AppError, Unit>
}
