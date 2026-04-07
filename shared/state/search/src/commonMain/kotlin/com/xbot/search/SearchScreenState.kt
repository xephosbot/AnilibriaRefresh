package com.xbot.search

import com.xbot.domain.models.Genre
import com.xbot.domain.models.enums.AgeRating
import com.xbot.domain.models.enums.ProductionStatus
import com.xbot.domain.models.enums.PublishStatus
import com.xbot.domain.models.enums.ReleaseType
import com.xbot.domain.models.enums.Season
import com.xbot.domain.models.enums.SortingType
import com.xbot.domain.models.filters.CatalogFilters
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class SearchScreenState(
    val query: String = "",
    @Transient val availableFilters: CatalogFilters = CatalogFilters.create(),
    @Transient val selectedGenres: Set<Genre> = emptySet(),
    val selectedReleaseTypes: Set<ReleaseType> = emptySet(),
    val selectedPublishStatuses: Set<PublishStatus> = emptySet(),
    val selectedProductionStatuses: Set<ProductionStatus> = emptySet(),
    val selectedSortingType: SortingType = SortingType.FRESH_AT_DESC,
    val selectedSeasons: Set<Season> = emptySet(),
    val selectedAgeRatings: Set<AgeRating> = emptySet(),
    @Transient val selectedYears: IntRange = IntRange.EMPTY,
) {
    val hasActiveFilters: Boolean
        get() = selectedGenres.isNotEmpty() ||
                selectedReleaseTypes.isNotEmpty() ||
                selectedPublishStatuses.isNotEmpty() ||
                selectedProductionStatuses.isNotEmpty() ||
                selectedSeasons.isNotEmpty() ||
                selectedAgeRatings.isNotEmpty()

    fun toSelectedFilters(): CatalogFilters = CatalogFilters(
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

fun CatalogFilters.isEmpty(): Boolean =
    genres.isEmpty() &&
            types.isEmpty() &&
            seasons.isEmpty() &&
            years == IntRange.EMPTY &&
            sortingTypes.isEmpty() &&
            ageRatings.isEmpty() &&
            publishStatuses.isEmpty() &&
            productionStatuses.isEmpty()
