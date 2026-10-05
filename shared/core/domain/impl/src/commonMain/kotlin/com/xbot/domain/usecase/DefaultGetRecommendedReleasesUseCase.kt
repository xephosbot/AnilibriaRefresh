package com.xbot.domain.usecase

import arrow.core.Either
import com.xbot.common.error.AppError
import com.xbot.data.repository.ReleasesRepository
import com.xbot.domain.models.Release
import kotlin.native.HiddenFromObjC
import org.koin.core.annotation.Factory

@Factory
@HiddenFromObjC
internal class DefaultGetRecommendedReleasesUseCase(
    private val releasesRepository: ReleasesRepository
) : GetRecommendedReleasesUseCase {
    override suspend fun invoke(): Either<AppError, List<Release>> =
        releasesRepository.getRandomReleases(10)
}
