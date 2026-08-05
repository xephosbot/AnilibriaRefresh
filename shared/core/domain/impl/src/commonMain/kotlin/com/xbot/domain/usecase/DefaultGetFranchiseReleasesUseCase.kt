package com.xbot.domain.usecase

import arrow.core.Either
import com.xbot.data.repository.FranchisesRepository
import com.xbot.common.error.AppError
import com.xbot.domain.models.Release
import org.koin.core.annotation.Factory
import kotlin.native.HiddenFromObjC

@Factory
@HiddenFromObjC
internal class DefaultGetFranchiseReleasesUseCase(
    private val franchisesRepository: FranchisesRepository,
) : GetFranchiseReleasesUseCase {
    override suspend fun invoke(aliasOrId: String): Either<AppError, List<Release>> =
        franchisesRepository.getFranchiseReleases(aliasOrId)
}
