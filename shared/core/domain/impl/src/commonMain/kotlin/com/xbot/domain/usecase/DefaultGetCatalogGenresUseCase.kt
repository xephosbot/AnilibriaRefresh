package com.xbot.domain.usecase

import arrow.core.Either
import com.xbot.data.repository.CatalogRepository
import com.xbot.domain.models.DomainError
import com.xbot.domain.models.Genre
import org.koin.core.annotation.Factory

@Factory
internal class DefaultGetCatalogGenresUseCase(
    private val catalogRepository: CatalogRepository,
) : GetCatalogGenresUseCase {
    override suspend fun invoke(): Either<DomainError, List<Genre>> =
        catalogRepository.getCatalogGenres()
}
