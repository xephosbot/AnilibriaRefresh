package com.xbot.domain.usecase

import arrow.core.Either
import com.xbot.domain.models.DomainError
import com.xbot.domain.models.Release

fun interface GetFranchiseReleasesUseCase {
    suspend operator fun invoke(aliasOrId: String): Either<DomainError, List<Release>>
}
