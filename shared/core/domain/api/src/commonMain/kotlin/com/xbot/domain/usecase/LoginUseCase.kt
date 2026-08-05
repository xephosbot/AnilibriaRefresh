package com.xbot.domain.usecase

import arrow.core.Either
import com.xbot.common.error.AppError
import kotlin.native.HiddenFromObjC

@HiddenFromObjC
fun interface LoginUseCase {
    suspend operator fun invoke(login: String, password: String): Either<AppError, Unit>
}
