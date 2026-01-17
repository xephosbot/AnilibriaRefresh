package com.xbot.fixtures.repository

import arrow.core.Either
import arrow.core.right
import com.xbot.domain.models.DomainError
import com.xbot.domain.models.enums.SocialType
import com.xbot.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow

class FakeAuthRepository : AuthRepository {
    private val _authState = MutableStateFlow(false)
    override val authState: Flow<Boolean> = flow {
        emit(false)
    }

    override suspend fun login(login: String, password: String): Either<DomainError, Unit> {
        _authState.value = true
        return Unit.right()
    }

    override suspend fun logout(): Either<DomainError, Unit> {
        _authState.value = false
        return Unit.right()
    }

    override suspend fun socialLogin(provider: SocialType): Either<DomainError, Unit> {
        _authState.value = true
        return Unit.right()
    }

    override suspend fun forgotPassword(email: String): Either<DomainError, Unit> {
        return Unit.right()
    }

    override suspend fun resetPassword(token: String, password: String, passwordConfirmation: String): Either<DomainError, Unit> {
        return Unit.right()
    }
}
