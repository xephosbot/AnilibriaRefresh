package com.xbot.domain.repository

import com.xbot.domain.model.AgeRatingEnumModel
import com.xbot.domain.model.GenreModel
import com.xbot.domain.model.ProductionStatusesEnumModel
import com.xbot.domain.model.PublishStatusEnumModel
import com.xbot.domain.model.ReleaseTypeEnumModel
import com.xbot.domain.model.SeasonEnumModel
import com.xbot.domain.model.SortingTypesEnumModel
import kotlinx.coroutines.flow.Flow

interface FiltersRepository {
    fun getAgeRatings(): Flow<List<AgeRatingEnumModel>>
    fun getGenres(): Flow<List<GenreModel>>
    fun getProductionStatuses(): Flow<List<ProductionStatusesEnumModel>>
    fun getPublishStatuses(): Flow<List<PublishStatusEnumModel>>
    fun getSeason(): Flow<List<SeasonEnumModel>>
    fun getSortingTypes(): Flow<List<SortingTypesEnumModel>>
    fun getTypeReleases(): Flow<List<ReleaseTypeEnumModel>>
    fun getYears(): Flow<List<Int>>
}