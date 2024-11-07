package com.xbot.data.repository

import com.xbot.data.datasource.FilterDataSource
import com.xbot.domain.model.AgeRatingEnumModel
import com.xbot.domain.model.GenreModel
import com.xbot.domain.model.ProductionStatusesEnumModel
import com.xbot.domain.model.PublishStatusEnumModel
import com.xbot.domain.model.ReleaseTypeEnumModel
import com.xbot.domain.model.SeasonEnumModel
import com.xbot.domain.model.SortingTypesEnumModel
import com.xbot.domain.repository.FiltersRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class FiltersRepositoryImpl @Inject constructor(
    private val filterDataSource: FilterDataSource
): FiltersRepository {

    override fun getAgeRatings(): Flow<List<AgeRatingEnumModel>> {
        return filterDataSource.getAgeRatings()
    }

    override fun getGenres(): Flow<List<GenreModel>> {
        return filterDataSource.getGenres()
    }

    override fun getProductionStatuses(): Flow<List<ProductionStatusesEnumModel>> {
        return filterDataSource.getProductionStatuses()
    }

    override fun getPublishStatuses(): Flow<List<PublishStatusEnumModel>> {
        return filterDataSource.getPublishStatuses()
    }

    override fun getSeason(): Flow<List<SeasonEnumModel>> {
        return filterDataSource.getSeasons()
    }

    override fun getSortingTypes(): Flow<List<SortingTypesEnumModel>> {
        return filterDataSource.getSortingTypes()
    }

    override fun getTypeReleases(): Flow<List<ReleaseTypeEnumModel>> {
        return filterDataSource.getReleaseType()
    }

    override fun getYears(): Flow<List<Int>> {
        return filterDataSource.getYears()
    }


}