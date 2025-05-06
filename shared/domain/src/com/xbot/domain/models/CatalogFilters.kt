package com.xbot.domain.models

import com.xbot.domain.models.enums.AgeRating
import com.xbot.domain.models.enums.ProductionStatus
import com.xbot.domain.models.enums.PublishStatus
import com.xbot.domain.models.enums.ReleaseType
import com.xbot.domain.models.enums.Season
import com.xbot.domain.models.enums.SortingType

data class CatalogFilters(
    val ageRatings: List<AgeRating>,
    val genres: List<Genre>,
    val productionStatuses: List<ProductionStatus>,
    val publishStatuses: List<PublishStatus>,
    val seasons: List<Season>,
    val sortingTypes: List<SortingType>,
    val releaseTypes: List<ReleaseType>,
    val years: ClosedRange<Int>
)