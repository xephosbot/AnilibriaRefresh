package com.xbot.domain.usecase

import arrow.core.Either
import com.xbot.data.repository.CatalogRepository
import com.xbot.domain.models.DomainError
import com.xbot.domain.models.enums.AgeRating
import org.koin.core.annotation.Factory

@Factory
internal class DefaultGetCatalogAgeRatingsUseCase(
    private val catalogRepository: CatalogRepository,
) : GetCatalogAgeRatingsUseCase {
    override suspend fun invoke(): Either<DomainError, List<AgeRating>> =
        catalogRepository.getCatalogAgeRatings()
}
