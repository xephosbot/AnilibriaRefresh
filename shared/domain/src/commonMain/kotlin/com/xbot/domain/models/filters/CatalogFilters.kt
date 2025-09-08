package com.xbot.domain.models.filters

import com.xbot.domain.models.Genre
import com.xbot.domain.models.enums.AgeRating
import com.xbot.domain.models.enums.ProductionStatus
import com.xbot.domain.models.enums.PublishStatus
import com.xbot.domain.models.enums.ReleaseType
import com.xbot.domain.models.enums.Season
import com.xbot.domain.models.enums.SortingType

data class CatalogFilters(
    val genres: List<Genre> = emptyList(),
    val types: List<ReleaseType> = emptyList(),
    val seasons: List<Season> = emptyList(),
    val years: IntRange = -1..-1,
    val sortingTypes: List<SortingType> = emptyList(),
    val ageRatings: List<AgeRating> = emptyList(),
    val publishStatuses: List<PublishStatus> = emptyList(),
    val productionStatuses: List<ProductionStatus> = emptyList(),
)