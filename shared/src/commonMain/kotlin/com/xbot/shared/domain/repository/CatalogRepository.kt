package com.xbot.shared.domain.repository

import com.xbot.shared.domain.models.Genre
import com.xbot.shared.domain.models.enums.AgeRating
import com.xbot.shared.domain.models.enums.ProductionStatus
import com.xbot.shared.domain.models.enums.PublishStatus
import com.xbot.shared.domain.models.enums.ReleaseType
import com.xbot.shared.domain.models.enums.Season
import com.xbot.shared.domain.models.enums.SortingType

interface CatalogRepository {
    suspend fun getAgeRatings(): Result<List<AgeRating>>
    suspend fun getGenres(): Result<List<Genre>>
    suspend fun getProductionStatuses(): Result<List<ProductionStatus>>
    suspend fun getPublishStatuses(): Result<List<PublishStatus>>
    suspend fun getSeasons(): Result<List<Season>>
    suspend fun getSortingTypes(): Result<List<SortingType>>
    suspend fun getReleaseTypes(): Result<List<ReleaseType>>
    suspend fun getYears(): Result<ClosedRange<Int>>
}
