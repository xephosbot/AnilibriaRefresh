package com.xbot.fixtures.repository

import androidx.paging.PagingSource
import arrow.core.Either
import arrow.core.right
import com.xbot.domain.models.DomainError
import com.xbot.domain.models.Genre
import com.xbot.domain.models.Release
import com.xbot.domain.models.enums.AgeRating
import com.xbot.domain.models.enums.ProductionStatus
import com.xbot.domain.models.enums.PublishStatus
import com.xbot.domain.models.enums.ReleaseType
import com.xbot.domain.models.enums.Season
import com.xbot.domain.models.enums.SortingType
import com.xbot.domain.models.filters.CatalogFilters
import com.xbot.domain.repository.CatalogRepository
import com.xbot.fixtures.data.genreMocks
import com.xbot.fixtures.data.releaseMocks

class FakeCatalogRepository : CatalogRepository {
    override fun getCatalogReleases(search: String?, filters: CatalogFilters?): PagingSource<Int, Release> {
        return FakePagingSource(releaseMocks)
    }

    override suspend fun getCatalogReleases(
        search: String?,
        filters: CatalogFilters?,
        limit: Int
    ): Either<DomainError, List<Release>> {
        return releaseMocks.take(limit).right()
    }

    override suspend fun getCatalogAgeRatings(): Either<DomainError, List<AgeRating>> {
        return AgeRating.entries.right()
    }

    override suspend fun getCatalogGenres(): Either<DomainError, List<Genre>> {
        return genreMocks.right()
    }

    override suspend fun getCatalogProductionStatuses(): Either<DomainError, List<ProductionStatus>> {
        return ProductionStatus.entries.right()
    }

    override suspend fun getCatalogPublishStatuses(): Either<DomainError, List<PublishStatus>> {
        return PublishStatus.entries.right()
    }

    override suspend fun getCatalogSeasons(): Either<DomainError, List<Season>> {
        return Season.entries.right()
    }

    override suspend fun getCatalogSortingTypes(): Either<DomainError, List<SortingType>> {
        return SortingType.entries.right()
    }

    override suspend fun getCatalogReleaseTypes(): Either<DomainError, List<ReleaseType>> {
        return ReleaseType.entries.right()
    }

    override suspend fun getCatalogYears(): Either<DomainError, IntRange> {
        return (1990..2024).right()
    }
}
