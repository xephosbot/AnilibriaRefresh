package com.xbot.data.repository

import com.xbot.api.client.AnilibriaClient
import com.xbot.api.models.shared.Genre
import com.xbot.api.models.shared.enums.AgeRatingEnum
import com.xbot.api.models.shared.enums.ProductionStatusEnum
import com.xbot.api.models.shared.enums.PublishStatusEnum
import com.xbot.api.models.shared.enums.ReleaseTypeEnum
import com.xbot.api.models.shared.enums.SeasonEnum
import com.xbot.api.models.shared.enums.SortingTypeEnum
import com.xbot.api.request.getCatalogAgeRatings
import com.xbot.api.request.getCatalogGenres
import com.xbot.api.request.getCatalogProductionStatuses
import com.xbot.api.request.getCatalogPublishStatuses
import com.xbot.api.request.getCatalogReleaseTypes
import com.xbot.api.request.getCatalogSeasons
import com.xbot.api.request.getCatalogSortingTypes
import com.xbot.api.request.getCatalogYears
import com.xbot.data.mapper.toAgeRating
import com.xbot.data.mapper.toGenreModel
import com.xbot.data.mapper.toProductionStatus
import com.xbot.data.mapper.toPublishStatus
import com.xbot.data.mapper.toReleaseType
import com.xbot.data.mapper.toSeason
import com.xbot.data.mapper.toSortingType
import com.xbot.domain.models.GenreModel
import com.xbot.domain.models.enums.AgeRating
import com.xbot.domain.models.enums.ProductionStatus
import com.xbot.domain.models.enums.PublishStatus
import com.xbot.domain.models.enums.ReleaseType
import com.xbot.domain.models.enums.Season
import com.xbot.domain.models.enums.SortingType
import com.xbot.domain.repository.FiltersRepository

class FiltersRepositoryImpl(
    private val client: AnilibriaClient
) : FiltersRepository {
    override suspend fun getAgeRatings(): List<AgeRating> {
        return client.getCatalogAgeRatings().map(AgeRatingEnum::toAgeRating)
    }

    override suspend fun getGenres(): List<GenreModel> {
        return client.getCatalogGenres().map(Genre::toGenreModel)
    }

    override suspend fun getProductionStatuses(): List<ProductionStatus> {
        return client.getCatalogProductionStatuses().map(ProductionStatusEnum::toProductionStatus)
    }

    override suspend fun getPublishStatuses(): List<PublishStatus> {
        return client.getCatalogPublishStatuses().map(PublishStatusEnum::toPublishStatus)
    }

    override suspend fun getSeasons(): List<Season> {
        return client.getCatalogSeasons().map(SeasonEnum::toSeason)
    }

    override suspend fun getSortingTypes(): List<SortingType> {
        return client.getCatalogSortingTypes().map(SortingTypeEnum::toSortingType)
    }

    override suspend fun getReleaseType(): List<ReleaseType> {
        return client.getCatalogReleaseTypes().map(ReleaseTypeEnum::toReleaseType)
    }

    override suspend fun getYears(): List<Int> {
        return client.getCatalogYears()
    }
}
