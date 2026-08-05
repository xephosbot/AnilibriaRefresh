package com.xbot.domain.usecase

import arrow.core.Either
import com.xbot.data.repository.CatalogRepository
import com.xbot.common.error.AppError
import com.xbot.domain.models.enums.SortingType
import org.koin.core.annotation.Factory
import kotlin.native.HiddenFromObjC

@Factory
@HiddenFromObjC
internal class DefaultGetCatalogSortingTypesUseCase(
    private val catalogRepository: CatalogRepository,
) : GetCatalogSortingTypesUseCase {
    override suspend fun invoke(): Either<AppError, List<SortingType>> =
        catalogRepository.getCatalogSortingTypes()
}
