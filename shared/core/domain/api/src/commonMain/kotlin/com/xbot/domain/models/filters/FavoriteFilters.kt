package com.xbot.domain.models.filters

import com.xbot.domain.models.Genre
import com.xbot.domain.models.enums.AgeRating
import com.xbot.domain.models.enums.ReleaseType
import com.xbot.domain.models.enums.SortingType

data class FavoriteFilters(
    val years: ClosedRange<Int>,
    val types: List<ReleaseType>,
    val genres: List<Genre>,
    val sortingTypes: List<SortingType>,
    val ageRatings: List<AgeRating>,
)
