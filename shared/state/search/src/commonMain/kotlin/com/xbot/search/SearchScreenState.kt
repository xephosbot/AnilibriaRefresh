package com.xbot.search

import com.xbot.common.AsyncResult
import com.xbot.domain.models.Genre
import com.xbot.domain.models.DomainError
import com.xbot.domain.models.enums.AgeRating
import com.xbot.domain.models.enums.ProductionStatus
import com.xbot.domain.models.enums.PublishStatus
import com.xbot.domain.models.enums.ReleaseType
import com.xbot.domain.models.enums.Season
import com.xbot.domain.models.enums.SortingType
import com.xbot.domain.models.filters.CatalogQuery
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class SearchScreenState(
    val query: String = "",
    @Transient val filters: SearchFiltersState = SearchFiltersState(),
    @Transient val genres: AsyncResult<DomainError, List<Genre>> = AsyncResult.Loading,
    @Transient val releaseTypes: AsyncResult<DomainError, List<ReleaseType>> = AsyncResult.Loading,
    @Transient val publishStatuses: AsyncResult<DomainError, List<PublishStatus>> = AsyncResult.Loading,
    @Transient val productionStatuses: AsyncResult<DomainError, List<ProductionStatus>> = AsyncResult.Loading,
    @Transient val sortingTypes: AsyncResult<DomainError, List<SortingType>> = AsyncResult.Loading,
    @Transient val seasons: AsyncResult<DomainError, List<Season>> = AsyncResult.Loading,
    @Transient val ageRatings: AsyncResult<DomainError, List<AgeRating>> = AsyncResult.Loading,
    @Transient val years: AsyncResult<DomainError, IntRange> = AsyncResult.Loading,
)

data class SearchFiltersState(
    val selectedSortingType: SortingType = SortingType.FRESH_AT_DESC,
    val selectedGenres: Set<Genre> = emptySet(),
    val selectedReleaseTypes: Set<ReleaseType> = emptySet(),
    val selectedPublishStatuses: Set<PublishStatus> = emptySet(),
    val selectedProductionStatuses: Set<ProductionStatus> = emptySet(),
    val selectedSeasons: Set<Season> = emptySet(),
    val selectedYears: IntRange = IntRange.EMPTY,
    val selectedAgeRatings: Set<AgeRating> = emptySet(),
) {
    val hasActiveFilters: Boolean
        get() = selectedGenres.isNotEmpty() ||
                selectedReleaseTypes.isNotEmpty() ||
                selectedPublishStatuses.isNotEmpty() ||
                selectedProductionStatuses.isNotEmpty() ||
                selectedSeasons.isNotEmpty() ||
                selectedAgeRatings.isNotEmpty()

    fun toCatalogQuery(): CatalogQuery = CatalogQuery(
        genres = selectedGenres.toList(),
        types = selectedReleaseTypes.toList(),
        publishStatuses = selectedPublishStatuses.toList(),
        productionStatuses = selectedProductionStatuses.toList(),
        sortingTypes = listOf(selectedSortingType),
        seasons = selectedSeasons.toList(),
        ageRatings = selectedAgeRatings.toList(),
        years = selectedYears,
    )
}
