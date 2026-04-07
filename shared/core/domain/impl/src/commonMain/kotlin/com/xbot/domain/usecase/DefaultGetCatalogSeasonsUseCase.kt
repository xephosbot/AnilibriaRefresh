package com.xbot.domain.usecase

import arrow.core.Either
import com.xbot.data.repository.CatalogRepository
import com.xbot.domain.models.DomainError
import com.xbot.domain.models.enums.Season
import org.koin.core.annotation.Factory

@Factory
internal class DefaultGetCatalogSeasonsUseCase(
    private val catalogRepository: CatalogRepository,
) : GetCatalogSeasonsUseCase {
    override suspend fun invoke(): Either<DomainError, List<Season>> =
        catalogRepository.getCatalogSeasons()
}
