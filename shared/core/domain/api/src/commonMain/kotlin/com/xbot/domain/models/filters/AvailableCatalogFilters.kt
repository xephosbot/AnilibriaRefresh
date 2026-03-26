package com.xbot.domain.models.filters

import com.xbot.domain.models.Genre
import com.xbot.domain.models.enums.AgeRating
import com.xbot.domain.models.enums.ProductionStatus
import com.xbot.domain.models.enums.PublishStatus
import com.xbot.domain.models.enums.ReleaseType
import com.xbot.domain.models.enums.Season
import com.xbot.domain.models.enums.SortingType
import io.nlopez.asyncresult.AsyncResult
import io.nlopez.asyncresult.Loading

data class AvailableCatalogFilters(
    val genres: AsyncResult<List<Genre>> = Loading,
    val types: AsyncResult<List<ReleaseType>> = Loading,
    val seasons: AsyncResult<List<Season>> = Loading,
    val years: AsyncResult<IntRange> = Loading,
    val sortingTypes: AsyncResult<List<SortingType>> = Loading,
    val ageRatings: AsyncResult<List<AgeRating>> = Loading,
    val publishStatuses: AsyncResult<List<PublishStatus>> = Loading,
    val productionStatuses: AsyncResult<List<ProductionStatus>> = Loading,
)
