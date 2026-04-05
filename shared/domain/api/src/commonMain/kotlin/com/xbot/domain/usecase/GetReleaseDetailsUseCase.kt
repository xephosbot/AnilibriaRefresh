package com.xbot.domain.usecase

import com.xbot.domain.models.ReleaseDetailsExtended
import com.xbot.domain.repository.FranchisesRepository
import com.xbot.domain.repository.ReleasesRepository
import com.xbot.common.DispatcherProvider
import com.xbot.common.combinePartial
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import org.koin.core.annotation.Factory

@Factory
class GetReleaseDetailsUseCase(
    private val releasesRepository: ReleasesRepository,
    private val franchisesRepository: FranchisesRepository,
    private val dispatcherProvider: DispatcherProvider,
) {
    operator fun invoke(aliasOrId: String): Flow<ReleaseDetailsExtended> = combinePartial(
        { releasesRepository.getRelease(aliasOrId) },
        { franchisesRepository.getFranchiseReleases(aliasOrId) }
    ) { details, relatedReleases ->
        ReleaseDetailsExtended.create(
            details = details,
            relatedReleases = relatedReleases
        )
    }.flowOn(dispatcherProvider.io)
}
