package com.xbot.domain.repository

import arrow.core.Either
import com.xbot.domain.models.Error
import com.xbot.domain.models.enums.SocialType
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(login: String, password: String): Either<Error, Unit>
    suspend fun logout(): Either<Error, Unit>
    suspend fun socialLogin(provider: SocialType): Either<Error, Unit>
    suspend fun forgotPassword(email: String): Either<Error, Unit>
    suspend fun resetPassword(token: String, password: String, passwordConfirmation: String): Either<Error, Unit>
    fun observeAuthState(): Flow<Boolean>
}