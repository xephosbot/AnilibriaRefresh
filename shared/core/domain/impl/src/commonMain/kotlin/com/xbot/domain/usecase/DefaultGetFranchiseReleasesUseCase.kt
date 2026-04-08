package com.xbot.domain.usecase

import arrow.core.Either
import com.xbot.data.repository.FranchisesRepository
import com.xbot.domain.models.DomainError
import com.xbot.domain.models.Release
import org.koin.core.annotation.Factory

@Factory
internal class DefaultGetFranchiseReleasesUseCase(
    private val franchisesRepository: FranchisesRepository,
) : GetFranchiseReleasesUseCase {
    override suspend fun invoke(aliasOrId: String): Either<DomainError, List<Release>> =
        franchisesRepository.getFranchiseReleases(aliasOrId)
}
