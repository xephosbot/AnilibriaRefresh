package com.xbot.domain.usecase

import arrow.core.Either
import com.xbot.data.repository.CatalogRepository
import com.xbot.domain.models.DomainError
import com.xbot.domain.models.Release
import com.xbot.domain.models.enums.SortingType
import com.xbot.domain.models.filters.CatalogQuery
import org.koin.core.annotation.Factory

@Factory
internal class DefaultGetBestReleasesForAllTimeUseCase(
    private val catalogRepository: CatalogRepository
) : GetBestReleasesForAllTimeUseCase {
    override suspend fun invoke(): Either<DomainError, List<Release>> {
        return catalogRepository.getCatalogReleases(
            search = null,
            filters = CatalogQuery(sortingTypes = listOf(SortingType.RATING_DESC)),
            limit = 10
        )
    }
}
