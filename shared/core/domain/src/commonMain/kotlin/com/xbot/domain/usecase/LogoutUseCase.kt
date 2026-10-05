package com.xbot.domain.usecase

import arrow.core.Either
import com.xbot.common.error.AppError
import com.xbot.domain.repository.AuthRepository
import kotlin.native.HiddenFromObjC
import org.koin.core.annotation.Factory

@Factory
@HiddenFromObjC
class LogoutUseCase(private val authRepository: AuthRepository) : EitherUseCase<Unit, Unit> {
    override suspend fun invoke(params: Unit): Either<AppError, Unit> = authRepository.logout()
}
