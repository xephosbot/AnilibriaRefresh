package com.xbot.domain.usecase

import arrow.core.Either
import com.xbot.common.error.AppError
import com.xbot.domain.models.ReleaseDetails
import com.xbot.domain.repository.ReleasesRepository
import kotlin.native.HiddenFromObjC
import org.koin.core.annotation.Factory

@Factory
@HiddenFromObjC
class GetReleaseUseCase(private val releasesRepository: ReleasesRepository) :
    EitherUseCase<GetReleaseUseCase.Params, ReleaseDetails> {
    data class Params(val aliasOrId: String)

    override suspend fun invoke(params: Params): Either<AppError, ReleaseDetails> =
        releasesRepository.getRelease(params.aliasOrId)
}
