package com.xbot.domain.repository

import arrow.core.Either
import com.xbot.domain.models.DomainError
import com.xbot.domain.models.enums.SocialType
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val authState: Flow<Boolean>
    suspend fun login(login: String, password: String): Either<DomainError, Unit>
    suspend fun logout(): Either<DomainError, Unit>
    suspend fun socialLogin(provider: SocialType): Either<DomainError, Unit>
    suspend fun forgotPassword(email: String): Either<DomainError, Unit>
    suspend fun resetPassword(token: String, password: String, passwordConfirmation: String): Either<DomainError, Unit>
}