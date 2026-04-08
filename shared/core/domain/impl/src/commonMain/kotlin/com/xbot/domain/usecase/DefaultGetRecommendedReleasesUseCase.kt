package com.xbot.domain.usecase

import arrow.core.Either
import com.xbot.data.repository.ReleasesRepository
import com.xbot.domain.models.DomainError
import com.xbot.domain.models.Release
import org.koin.core.annotation.Factory

@Factory
internal class DefaultGetRecommendedReleasesUseCase(
    private val releasesRepository: ReleasesRepository,
) : GetRecommendedReleasesUseCase {
    override suspend fun invoke(): Either<DomainError, List<Release>> {
        return releasesRepository.getRandomReleases(10)
    }
}
