package com.xbot.network.api

import arrow.core.Either
import com.xbot.domain.models.DomainError
import com.xbot.network.models.enums.SocialTypeDto
import com.xbot.network.models.responses.AuthResponse
import com.xbot.network.models.responses.LoginResponse
import com.xbot.network.models.responses.LogoutResponse
import com.xbot.network.models.responses.SocialAuthResponse

interface AuthApi {
    suspend fun login(login: String, password: String): Either<DomainError, LoginResponse>
    suspend fun logout(): Either<DomainError, LogoutResponse>
    suspend fun socialLogin(provider: SocialTypeDto): Either<DomainError, SocialAuthResponse>
    suspend fun socialAuthenticate(state: String): Either<DomainError, AuthResponse>
    suspend fun forgotPassword(email: String): Either<DomainError, Unit>
    suspend fun resetPassword(token: String, password: String, passwordConfirmation: String): Either<DomainError, Unit>
}
