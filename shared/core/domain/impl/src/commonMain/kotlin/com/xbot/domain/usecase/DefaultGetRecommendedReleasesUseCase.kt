package com.xbot.domain.usecase

import arrow.core.Either
import com.xbot.data.repository.ReleasesRepository
import com.xbot.common.error.AppError
import com.xbot.domain.models.Release
import org.koin.core.annotation.Factory
import kotlin.native.HiddenFromObjC

@Factory
@HiddenFromObjC
internal class DefaultGetRecommendedReleasesUseCase(
    private val releasesRepository: ReleasesRepository,
) : GetRecommendedReleasesUseCase {
    override suspend fun invoke(): Either<AppError, List<Release>> {
        return releasesRepository.getRandomReleases(10)
    }
}
