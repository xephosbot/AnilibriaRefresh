package com.xbot.domain.repository

import com.xbot.domain.models.Genre
import com.xbot.domain.models.enums.AgeRating
import com.xbot.domain.models.enums.ProductionStatus
import com.xbot.domain.models.enums.PublishStatus
import com.xbot.domain.models.enums.ReleaseType
import com.xbot.domain.models.enums.Season
import com.xbot.domain.models.enums.SortingType

interface CatalogRepository {
    suspend fun getAgeRatings(): Result<List<AgeRating>>
    suspend fun getGenres(): Result<List<Genre>>
    suspend fun getProductionStatuses(): Result<List<ProductionStatus>>
    suspend fun getPublishStatuses(): Result<List<PublishStatus>>
    suspend fun getSeasons(): Result<List<Season>>
    suspend fun getSortingTypes(): Result<List<SortingType>>
    suspend fun getReleaseTypes(): Result<List<ReleaseType>>
    suspend fun getYears(): Result<List<Int>>
}
