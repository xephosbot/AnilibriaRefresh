package com.xbot.data.repository

import arrow.core.Either
import com.xbot.common.error.AppError
import com.xbot.domain.models.enums.SocialType
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val authState: Flow<Boolean>
    suspend fun login(login: String, password: String): Either<AppError, Unit>
    suspend fun logout(): Either<AppError, Unit>
    suspend fun socialLogin(provider: SocialType): Either<AppError, Unit>
    suspend fun forgotPassword(email: String): Either<AppError, Unit>
    suspend fun resetPassword(token: String, password: String, passwordConfirmation: String): Either<AppError, Unit>
}