package com.xbot.domain.usecase

import arrow.core.Either
import com.xbot.data.repository.CatalogRepository
import com.xbot.domain.models.DomainError
import com.xbot.domain.models.enums.ReleaseType
import org.koin.core.annotation.Factory

@Factory
internal class DefaultGetCatalogReleaseTypesUseCase(
    private val catalogRepository: CatalogRepository,
) : GetCatalogReleaseTypesUseCase {
    override suspend fun invoke(): Either<DomainError, List<ReleaseType>> =
        catalogRepository.getCatalogReleaseTypes()
}
