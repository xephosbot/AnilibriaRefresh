package com.xbot.domain.usecase

import arrow.core.Either
import com.xbot.common.error.AppError
import com.xbot.data.repository.AuthRepository
import kotlin.native.HiddenFromObjC
import org.koin.core.annotation.Factory

@Factory
@HiddenFromObjC
class DefaultLogoutUseCase(private val authRepository: AuthRepository) : LogoutUseCase {
    override suspend fun invoke(): Either<AppError, Unit> = authRepository.logout()
}
