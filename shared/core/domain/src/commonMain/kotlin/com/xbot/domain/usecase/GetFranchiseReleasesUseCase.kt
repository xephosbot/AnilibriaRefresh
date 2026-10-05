package com.xbot.domain.usecase

import arrow.core.Either
import com.xbot.common.error.AppError
import com.xbot.domain.models.Release
import com.xbot.domain.repository.FranchisesRepository
import kotlin.native.HiddenFromObjC
import org.koin.core.annotation.Factory

@Factory
@HiddenFromObjC
class GetFranchiseReleasesUseCase(private val franchisesRepository: FranchisesRepository) :
    EitherUseCase<GetFranchiseReleasesUseCase.Params, List<Release>> {
    data class Params(val aliasOrId: String)

    override suspend fun invoke(params: Params): Either<AppError, List<Release>> =
        franchisesRepository.getFranchiseReleases(params.aliasOrId)
}
