package com.xbot.domain.repository

import com.xbot.domain.models.GenreModel
import com.xbot.domain.models.enums.AgeRating
import com.xbot.domain.models.enums.ProductionStatus
import com.xbot.domain.models.enums.PublishStatus
import com.xbot.domain.models.enums.ReleaseType
import com.xbot.domain.models.enums.Season
import com.xbot.domain.models.enums.SortingTypes
import kotlinx.coroutines.flow.Flow

interface FiltersRepository {
    fun getAgeRatings(): Flow<List<AgeRating>>
    fun getGenres(): Flow<List<GenreModel>>
    fun getProductionStatuses(): Flow<List<ProductionStatus>>
    fun getPublishStatuses(): Flow<List<PublishStatus>>
    fun getSeason(): Flow<List<Season>>
    fun getSortingTypes(): Flow<List<SortingTypes>>
    fun getTypeReleases(): Flow<List<ReleaseType>>
    fun getYears(): Flow<List<Int>>
}
