package com.xbot.domain.models.filters

import com.xbot.domain.models.Genre
import com.xbot.domain.models.enums.AgeRating
import com.xbot.domain.models.enums.ProductionStatus
import com.xbot.domain.models.enums.PublishStatus
import com.xbot.domain.models.enums.ReleaseType
import com.xbot.domain.models.enums.Season
import com.xbot.domain.models.enums.SortingType

data class CatalogFilters(
    val genres: List<Genre>,
    val types: List<ReleaseType>,
    val seasons: List<Season>,
    val years: IntRange,
    val sortingTypes: List<SortingType>,
    val ageRatings: List<AgeRating>,
    val publishStatuses: List<PublishStatus>,
    val productionStatuses: List<ProductionStatus>,
) {
    companion object {
        fun create(
            genres: List<Genre>? = null,
            types: List<ReleaseType>? = null,
            seasons: List<Season>? = null,
            years: IntRange? = null,
            sortingTypes: List<SortingType>? = null,
            ageRatings: List<AgeRating>? = null,
            publishStatuses: List<PublishStatus>? = null,
            productionStatuses: List<ProductionStatus>? = null,
        ) = CatalogFilters(
            genres = genres ?: emptyList(),
            types = types ?: emptyList(),
            seasons = seasons ?: emptyList(),
            years = years ?: IntRange.EMPTY,
            sortingTypes = sortingTypes ?: emptyList(),
            ageRatings = ageRatings ?: emptyList(),
            publishStatuses = publishStatuses ?: emptyList(),
            productionStatuses = productionStatuses ?: emptyList(),
        )
    }
}
