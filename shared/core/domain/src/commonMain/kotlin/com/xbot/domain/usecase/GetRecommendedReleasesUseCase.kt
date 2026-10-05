package com.xbot.domain.usecase

import arrow.core.Either
import com.xbot.common.error.AppError
import com.xbot.domain.models.Release
import com.xbot.domain.repository.ReleasesRepository
import kotlin.native.HiddenFromObjC
import org.koin.core.annotation.Factory

@Factory
@HiddenFromObjC
class GetRecommendedReleasesUseCase(private val releasesRepository: ReleasesRepository) :
    EitherUseCase<Unit, List<Release>> {
    override suspend fun invoke(params: Unit): Either<AppError, List<Release>> =
        releasesRepository.getRandomReleases(RELEASES_COUNT)

    private companion object {
        const val RELEASES_COUNT = 10
    }
}
