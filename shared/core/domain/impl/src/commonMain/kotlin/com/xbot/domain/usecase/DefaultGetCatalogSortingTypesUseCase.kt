package com.xbot.domain.usecase

import arrow.core.Either
import com.xbot.common.error.AppError
import com.xbot.data.repository.CatalogRepository
import com.xbot.domain.models.enums.SortingType
import kotlin.native.HiddenFromObjC
import org.koin.core.annotation.Factory

@Factory
@HiddenFromObjC
internal class DefaultGetCatalogSortingTypesUseCase(
    private val catalogRepository: CatalogRepository
) : GetCatalogSortingTypesUseCase {
    override suspend fun invoke(): Either<AppError, List<SortingType>> =
        catalogRepository.getCatalogSortingTypes()
}
