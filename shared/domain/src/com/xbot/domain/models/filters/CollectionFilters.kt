package com.xbot.domain.models.filters

import com.xbot.domain.models.Genre
import com.xbot.domain.models.enums.AgeRating
import com.xbot.domain.models.enums.CollectionType
import com.xbot.domain.models.enums.ReleaseType

data class CollectionFilters(
    val collectionTypes: List<CollectionType>,
    val genres: List<Genre>,
    val types: List<ReleaseType>,
    val years: ClosedRange<Int>,
    val ageRatings: List<AgeRating>,
)
