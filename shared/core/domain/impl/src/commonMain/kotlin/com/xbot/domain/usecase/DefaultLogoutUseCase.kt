package com.xbot.domain.usecase

import arrow.core.Either
import com.xbot.data.repository.AuthRepository
import com.xbot.common.error.AppError
import org.koin.core.annotation.Factory
import kotlin.native.HiddenFromObjC

@Factory
@HiddenFromObjC
class DefaultLogoutUseCase(
    private val authRepository: AuthRepository,
) : LogoutUseCase {
    override suspend fun invoke(): Either<AppError, Unit> =
        authRepository.logout()
}
