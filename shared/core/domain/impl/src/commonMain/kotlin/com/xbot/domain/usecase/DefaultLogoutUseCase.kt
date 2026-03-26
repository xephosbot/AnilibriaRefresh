package com.xbot.domain.usecase

import arrow.core.Either
import com.xbot.data.repository.AuthRepository
import com.xbot.domain.models.DomainError
import org.koin.core.annotation.Factory

@Factory
class DefaultLogoutUseCase(
    private val authRepository: AuthRepository,
) : LogoutUseCase {
    override suspend fun invoke(): Either<DomainError, Unit> =
        authRepository.logout()
}
