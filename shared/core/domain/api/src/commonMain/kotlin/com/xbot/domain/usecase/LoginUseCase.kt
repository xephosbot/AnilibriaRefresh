package com.xbot.domain.usecase

import arrow.core.Either
import com.xbot.domain.models.DomainError
import kotlin.native.HiddenFromObjC

@HiddenFromObjC
fun interface LoginUseCase {
    suspend operator fun invoke(login: String, password: String): Either<DomainError, Unit>
}
