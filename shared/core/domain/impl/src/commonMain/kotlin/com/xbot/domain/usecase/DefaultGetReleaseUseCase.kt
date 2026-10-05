package com.xbot.domain.usecase

import arrow.core.Either
import com.xbot.common.error.AppError
import com.xbot.data.repository.ReleasesRepository
import com.xbot.domain.models.ReleaseDetails
import kotlin.native.HiddenFromObjC
import org.koin.core.annotation.Factory

@Factory
@HiddenFromObjC
class DefaultGetReleaseUseCase(private val releasesRepository: ReleasesRepository) :
    GetReleaseUseCase {
    override suspend fun invoke(aliasOrId: String): Either<AppError, ReleaseDetails> =
        releasesRepository.getRelease(aliasOrId)
}
