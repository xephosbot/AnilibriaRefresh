package com.xbot.domain.usecase

import arrow.core.Either
import com.xbot.domain.models.DomainError
import com.xbot.domain.models.Release

fun interface GetRecommendedReleasesUseCase {
    suspend operator fun invoke(): Either<DomainError, List<Release>>
}