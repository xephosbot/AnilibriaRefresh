package com.xbot.domain.usecase

import arrow.core.Either
import com.xbot.domain.models.DomainError
import com.xbot.domain.models.ReleaseDetails

fun interface GetReleaseUseCase {
    suspend operator fun invoke(aliasOrId: String): Either<DomainError, ReleaseDetails>
}
