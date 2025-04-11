package com.xbot.shared.domain.models

import com.xbot.shared.domain.models.enums.AgeRating
import com.xbot.shared.domain.models.enums.ProductionStatus
import com.xbot.shared.domain.models.enums.PublishStatus
import com.xbot.shared.domain.models.enums.ReleaseType
import com.xbot.shared.domain.models.enums.Season
import com.xbot.shared.domain.models.enums.SortingType

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