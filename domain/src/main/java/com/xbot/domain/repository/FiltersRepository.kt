package com.xbot.domain.repository

import com.xbot.domain.models.GenreModel
import com.xbot.domain.models.enums.AgeRating
import com.xbot.domain.models.enums.ProductionStatus
import com.xbot.domain.models.enums.PublishStatus
import com.xbot.domain.models.enums.ReleaseType
import com.xbot.domain.models.enums.Season
import com.xbot.domain.models.enums.SortingType

interface FiltersRepository {
    suspend fun getAgeRatings(): List<AgeRating>
    suspend fun getGenres(): List<GenreModel>
    suspend fun getProductionStatuses(): List<ProductionStatus>
    suspend fun getPublishStatuses(): List<PublishStatus>
    suspend fun getSeason(): List<Season>
    suspend fun getSortingTypes(): List<SortingType>
    suspend fun getTypeReleases(): List<ReleaseType>
    suspend fun getYears(): List<Int>
}
