package com.xbot.domain.usecase

import arrow.core.Either
import com.xbot.data.repository.CatalogRepository
import com.xbot.domain.models.DomainError
import org.koin.core.annotation.Factory

@Factory
internal class DefaultGetCatalogYearsUseCase(
    private val catalogRepository: CatalogRepository,
) : GetCatalogYearsUseCase {
    override suspend fun invoke(): Either<DomainError, IntRange> =
        catalogRepository.getCatalogYears()
}
