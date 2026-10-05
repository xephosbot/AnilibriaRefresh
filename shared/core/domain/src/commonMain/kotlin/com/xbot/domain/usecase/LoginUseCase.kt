package com.xbot.domain.usecase

import arrow.core.Either
import com.xbot.common.error.AppError
import com.xbot.domain.repository.AuthRepository
import kotlin.native.HiddenFromObjC
import org.koin.core.annotation.Factory

@Factory
@HiddenFromObjC
class LoginUseCase(private val authRepository: AuthRepository) :
    EitherUseCase<LoginUseCase.Params, Unit> {
    data class Params(val login: String, val password: String)

    override suspend fun invoke(params: Params): Either<AppError, Unit> =
        authRepository.login(params.login, params.password)
}
