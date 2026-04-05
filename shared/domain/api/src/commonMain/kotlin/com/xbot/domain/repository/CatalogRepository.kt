package com.xbot.domain.repository

import androidx.paging.PagingSource
import arrow.core.Either
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

interface CatalogRepository {
    fun getCatalogReleases(search: String? = null, filters: CatalogFilters? = null): PagingSource<Int, Release>
    suspend fun getCatalogReleases(search: String? = null, filters: CatalogFilters? = null, limit: Int): Either<DomainError, List<Release>>
    suspend fun getCatalogAgeRatings(): Either<DomainError, List<AgeRating>>
    suspend fun getCatalogGenres(): Either<DomainError, List<Genre>>
    suspend fun getCatalogProductionStatuses(): Either<DomainError, List<ProductionStatus>>
    suspend fun getCatalogPublishStatuses(): Either<DomainError, List<PublishStatus>>
    suspend fun getCatalogSeasons(): Either<DomainError, List<Season>>
    suspend fun getCatalogSortingTypes(): Either<DomainError, List<SortingType>>
    suspend fun getCatalogReleaseTypes(): Either<DomainError, List<ReleaseType>>
    suspend fun getCatalogYears(): Either<DomainError, IntRange>
}
