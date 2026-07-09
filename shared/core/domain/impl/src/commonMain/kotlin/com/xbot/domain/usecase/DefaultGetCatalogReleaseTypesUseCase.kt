package com.xbot.domain.usecase

import arrow.core.Either
import com.xbot.data.repository.CatalogRepository
import com.xbot.common.error.AppError
import com.xbot.domain.models.enums.ReleaseType
import org.koin.core.annotation.Factory
import kotlin.native.HiddenFromObjC

@Factory
@HiddenFromObjC
internal class DefaultGetCatalogReleaseTypesUseCase(
    private val catalogRepository: CatalogRepository,
) : GetCatalogReleaseTypesUseCase {
    override suspend fun invoke(): Either<AppError, List<ReleaseType>> =
        catalogRepository.getCatalogReleaseTypes()
}
