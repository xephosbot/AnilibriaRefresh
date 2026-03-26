package com.xbot.domain.usecase

import arrow.core.Either
import com.xbot.domain.models.DomainError

fun interface LoginUseCase {
    suspend operator fun invoke(login: String, password: String): Either<DomainError, Unit>
}
