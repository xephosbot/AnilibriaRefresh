package com.xbot.data.repository

import com.xbot.data.datasource.FiltersDataSource
import com.xbot.domain.models.GenreModel
import com.xbot.domain.models.enums.AgeRating
import com.xbot.domain.models.enums.ProductionStatus
import com.xbot.domain.models.enums.PublishStatus
import com.xbot.domain.models.enums.ReleaseType
import com.xbot.domain.models.enums.Season
import com.xbot.domain.models.enums.SortingTypes
import com.xbot.domain.repository.FiltersRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class FiltersRepositoryImpl @Inject constructor(
    private val filterDataSource: FiltersDataSource,
) : FiltersRepository {
    override fun getAgeRatings(): Flow<List<AgeRating>> {
        return filterDataSource.getAgeRatings()
    }

    override fun getGenres(): Flow<List<GenreModel>> {
        return filterDataSource.getGenres()
    }

    override fun getProductionStatuses(): Flow<List<ProductionStatus>> {
        return filterDataSource.getProductionStatuses()
    }

    override fun getPublishStatuses(): Flow<List<PublishStatus>> {
        return filterDataSource.getPublishStatuses()
    }

    override fun getSeason(): Flow<List<Season>> {
        return filterDataSource.getSeasons()
    }

    override fun getSortingTypes(): Flow<List<SortingTypes>> {
        return filterDataSource.getSortingTypes()
    }

    override fun getTypeReleases(): Flow<List<ReleaseType>> {
        return filterDataSource.getReleaseType()
    }

    override fun getYears(): Flow<List<Int>> {
        return filterDataSource.getYears()
    }
}
