package com.xbot.domain.repository

import com.xbot.domain.models.GenreModel
import com.xbot.domain.models.enums.AgeRating
import com.xbot.domain.models.enums.ProductionStatus
import com.xbot.domain.models.enums.PublishStatus
import com.xbot.domain.models.enums.ReleaseType
import com.xbot.domain.models.enums.Season
import com.xbot.domain.models.enums.SortingType

interface FiltersRepository {
    suspend fun getAgeRatings(): Result<List<AgeRating>>
    suspend fun getGenres(): Result<List<GenreModel>>
    suspend fun getProductionStatuses(): Result<List<ProductionStatus>>
    suspend fun getPublishStatuses(): Result<List<PublishStatus>>
    suspend fun getSeasons(): Result<List<Season>>
    suspend fun getSortingTypes(): Result<List<SortingType>>
    suspend fun getReleaseType(): Result<List<ReleaseType>>
    suspend fun getYears(): Result<List<Int>>
}
