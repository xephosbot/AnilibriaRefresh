package com.xbot.domain.usecase

import arrow.core.Either
import com.xbot.common.error.AppError
import com.xbot.domain.models.enums.ReleaseType
import com.xbot.domain.repository.CatalogRepository
import kotlin.native.HiddenFromObjC
import org.koin.core.annotation.Factory

@Factory
@HiddenFromObjC
class GetCatalogReleaseTypesUseCase(private val catalogRepository: CatalogRepository) :
    EitherUseCase<Unit, List<ReleaseType>> {
    override suspend fun invoke(params: Unit): Either<AppError, List<ReleaseType>> =
        catalogRepository.getCatalogReleaseTypes()
}
