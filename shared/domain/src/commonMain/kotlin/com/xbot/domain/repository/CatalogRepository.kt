package com.xbot.domain.repository

import androidx.paging.PagingSource
import arrow.core.Either
import com.xbot.domain.models.Error
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
    suspend fun getCatalogAgeRatings(): Either<Error, List<AgeRating>>
    suspend fun getCatalogGenres(): Either<Error, List<Genre>>
    suspend fun getCatalogProductionStatuses(): Either<Error, List<ProductionStatus>>
    suspend fun getCatalogPublishStatuses(): Either<Error, List<PublishStatus>>
    suspend fun getCatalogSeasons(): Either<Error, List<Season>>
    suspend fun getCatalogSortingTypes(): Either<Error, List<SortingType>>
    suspend fun getCatalogReleaseTypes(): Either<Error, List<ReleaseType>>
    suspend fun getCatalogYears(): Either<Error, ClosedRange<Int>>
}
