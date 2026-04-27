package com.xbot.data.fixtures

import androidx.paging.PagingSource
import arrow.core.Either
import arrow.core.right
import com.xbot.data.repository.CatalogRepository
import com.xbot.domain.fixtures.GenreFixtures
import com.xbot.domain.fixtures.ReleaseFixtures
import com.xbot.domain.models.DomainError
import com.xbot.domain.models.Genre
import com.xbot.domain.models.Release
import com.xbot.domain.models.enums.AgeRating
import com.xbot.domain.models.enums.ProductionStatus
import com.xbot.domain.models.enums.PublishStatus
import com.xbot.domain.models.enums.ReleaseType
import com.xbot.domain.models.enums.Season
import com.xbot.domain.models.enums.SortingType
import com.xbot.domain.models.filters.CatalogQuery

class FakeCatalogRepository : CatalogRepository {
    override fun getCatalogReleases(search: String?, filters: CatalogQuery?): PagingSource<Int, Release> {
        return FakePagingSource(ReleaseFixtures.all)
    }

    override suspend fun getCatalogReleases(
        search: String?,
        filters: CatalogQuery?,
        limit: Int
    ): Either<DomainError, List<Release>> {
        return ReleaseFixtures.all.take(limit).right()
    }

    override suspend fun getCatalogAgeRatings(): Either<DomainError, List<AgeRating>> {
        return AgeRating.entries.right()
    }

    override suspend fun getCatalogGenres(): Either<DomainError, List<Genre>> {
        return GenreFixtures.all.right()
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
