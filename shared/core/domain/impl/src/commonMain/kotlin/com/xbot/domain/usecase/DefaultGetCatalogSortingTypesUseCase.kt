package com.xbot.domain.usecase

import arrow.core.Either
import com.xbot.data.repository.CatalogRepository
import com.xbot.domain.models.DomainError
import com.xbot.domain.models.enums.SortingType
import org.koin.core.annotation.Factory

@Factory
internal class DefaultGetCatalogSortingTypesUseCase(
    private val catalogRepository: CatalogRepository,
) : GetCatalogSortingTypesUseCase {
    override suspend fun invoke(): Either<DomainError, List<SortingType>> =
        catalogRepository.getCatalogSortingTypes()
}
