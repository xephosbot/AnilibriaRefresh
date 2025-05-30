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
    val years: ClosedRange<Int>,
    val sortingTypes: List<SortingType>,
    val ageRatings: List<AgeRating>,
    val publishStatuses: List<PublishStatus>,
    val productionStatuses: List<ProductionStatus>,
)