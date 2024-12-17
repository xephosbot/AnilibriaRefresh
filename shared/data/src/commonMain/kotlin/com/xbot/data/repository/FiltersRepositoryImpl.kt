package com.xbot.data.repository

import com.xbot.data.datasource.FiltersDataSource
import com.xbot.domain.models.GenreModel
import com.xbot.domain.models.enums.AgeRating
import com.xbot.domain.models.enums.ProductionStatus
import com.xbot.domain.models.enums.PublishStatus
import com.xbot.domain.models.enums.ReleaseType
import com.xbot.domain.models.enums.Season
import com.xbot.domain.models.enums.SortingType
import com.xbot.domain.repository.FiltersRepository

class FiltersRepositoryImpl(
    private val filterDataSource: FiltersDataSource,
) : FiltersRepository {
    override suspend fun getAgeRatings(): List<AgeRating> {
        return filterDataSource.getAgeRatings()
    }

    override suspend fun getGenres(): List<GenreModel> {
        return filterDataSource.getGenres()
    }

    override suspend fun getProductionStatuses(): List<ProductionStatus> {
        return filterDataSource.getProductionStatuses()
    }

    override suspend fun getPublishStatuses(): List<PublishStatus> {
        return filterDataSource.getPublishStatuses()
    }

    override suspend fun getSeason(): List<Season> {
        return filterDataSource.getSeasons()
    }

    override suspend fun getSortingTypes(): List<SortingType> {
        return filterDataSource.getSortingTypes()
    }

    override suspend fun getTypeReleases(): List<ReleaseType> {
        return filterDataSource.getReleaseType()
    }

    override suspend fun getYears(): List<Int> {
        return filterDataSource.getYears()
    }
}
