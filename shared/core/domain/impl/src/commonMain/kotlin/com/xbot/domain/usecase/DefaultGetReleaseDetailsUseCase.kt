package com.xbot.domain.usecase

import com.xbot.common.DispatcherProvider
import com.xbot.common.combinePartial
import com.xbot.data.repository.FranchisesRepository
import com.xbot.data.repository.ReleasesRepository
import com.xbot.domain.models.ReleaseDetailsExtended
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import org.koin.core.annotation.Factory

@Factory
internal class DefaultGetReleaseDetailsUseCase(
    private val releasesRepository: ReleasesRepository,
    private val franchisesRepository: FranchisesRepository,
    private val dispatcherProvider: DispatcherProvider,
) : GetReleaseDetailsUseCase {
    override fun invoke(aliasOrId: String): Flow<ReleaseDetailsExtended> = combinePartial(
        { releasesRepository.getRelease(aliasOrId) },
        { franchisesRepository.getFranchiseReleases(aliasOrId) }
    ) { details, relatedReleases ->
        ReleaseDetailsExtended.create(
            details = details,
            relatedReleases = relatedReleases
        )
    }.flowOn(dispatcherProvider.io)
}
