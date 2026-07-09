package com.xbot.domain.usecase

import arrow.core.Either
import com.xbot.data.repository.AuthRepository
import com.xbot.common.error.AppError
import org.koin.core.annotation.Factory
import kotlin.native.HiddenFromObjC

@Factory
@HiddenFromObjC
class DefaultLoginUseCase(
    private val authRepository: AuthRepository,
) : LoginUseCase {
    override suspend fun invoke(login: String, password: String): Either<AppError, Unit> =
        authRepository.login(login, password)
}
