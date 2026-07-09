package com.xbot.domain.usecase

import arrow.core.Either
import com.xbot.data.repository.ReleasesRepository
import com.xbot.common.error.AppError
import com.xbot.domain.models.ReleaseDetails
import org.koin.core.annotation.Factory
import kotlin.native.HiddenFromObjC

@Factory
@HiddenFromObjC
class DefaultGetReleaseUseCase(
    private val releasesRepository: ReleasesRepository,
) : GetReleaseUseCase {
    override suspend fun invoke(aliasOrId: String): Either<AppError, ReleaseDetails> =
        releasesRepository.getRelease(aliasOrId)
}
