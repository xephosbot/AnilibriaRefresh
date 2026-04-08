package com.xbot.domain.usecase

import arrow.core.Either
import com.xbot.data.repository.CatalogRepository
import com.xbot.domain.models.DomainError
import com.xbot.domain.models.enums.ProductionStatus
import org.koin.core.annotation.Factory

@Factory
internal class DefaultGetCatalogProductionStatusesUseCase(
    private val catalogRepository: CatalogRepository,
) : GetCatalogProductionStatusesUseCase {
    override suspend fun invoke(): Either<DomainError, List<ProductionStatus>> =
        catalogRepository.getCatalogProductionStatuses()
}
