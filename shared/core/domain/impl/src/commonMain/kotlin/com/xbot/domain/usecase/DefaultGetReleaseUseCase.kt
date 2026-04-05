package com.xbot.domain.usecase

import arrow.core.Either
import com.xbot.data.repository.ReleasesRepository
import com.xbot.domain.models.DomainError
import com.xbot.domain.models.ReleaseDetails
import org.koin.core.annotation.Factory

@Factory
class DefaultGetReleaseUseCase(
    private val releasesRepository: ReleasesRepository,
) : GetReleaseUseCase {
    override suspend fun invoke(aliasOrId: String): Either<DomainError, ReleaseDetails> =
        releasesRepository.getRelease(aliasOrId)
}
