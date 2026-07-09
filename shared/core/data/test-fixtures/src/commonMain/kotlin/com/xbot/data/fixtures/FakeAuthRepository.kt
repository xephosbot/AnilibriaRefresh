package com.xbot.data.fixtures

import arrow.core.Either
import arrow.core.right
import com.xbot.data.repository.AuthRepository
import com.xbot.common.error.AppError
import com.xbot.domain.models.enums.SocialType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow

class FakeAuthRepository : AuthRepository {
    private val _authState = MutableStateFlow(false)
    override val authState: Flow<Boolean> = flow {
        emit(false)
    }

    override suspend fun login(login: String, password: String): Either<AppError, Unit> {
        _authState.value = true
        return Unit.right()
    }

    override suspend fun logout(): Either<AppError, Unit> {
        _authState.value = false
        return Unit.right()
    }

    override suspend fun socialLogin(provider: SocialType): Either<AppError, Unit> {
        _authState.value = true
        return Unit.right()
    }

    override suspend fun forgotPassword(email: String): Either<AppError, Unit> {
        return Unit.right()
    }

    override suspend fun resetPassword(token: String, password: String, passwordConfirmation: String): Either<AppError, Unit> {
        return Unit.right()
    }
}
