package com.xbot.domain.repository

import androidx.paging.PagingSource
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
    suspend fun getCatalogAgeRatings(): Result<List<AgeRating>>
    suspend fun getCatalogGenres(): Result<List<Genre>>
    suspend fun getCatalogProductionStatuses(): Result<List<ProductionStatus>>
    suspend fun getCatalogPublishStatuses(): Result<List<PublishStatus>>
    suspend fun getCatalogSeasons(): Result<List<Season>>
    suspend fun getCatalogSortingTypes(): Result<List<SortingType>>
    suspend fun getCatalogReleaseTypes(): Result<List<ReleaseType>>
    suspend fun getCatalogYears(): Result<ClosedRange<Int>>
}
