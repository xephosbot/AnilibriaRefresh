package com.xbot.domain.usecase

import arrow.core.Either
import com.xbot.data.repository.AuthRepository
import com.xbot.domain.models.DomainError
import org.koin.core.annotation.Factory

@Factory
class DefaultLoginUseCase(
    private val authRepository: AuthRepository,
) : LoginUseCase {
    override suspend fun invoke(login: String, password: String): Either<DomainError, Unit> =
        authRepository.login(login, password)
}
