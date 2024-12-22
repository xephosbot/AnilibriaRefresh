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

internal class DefaultFiltersRepository(
    private val client: AnilibriaClient
) : FiltersRepository {
    override suspend fun getAgeRatings(): Result<List<AgeRating>> = runCatching {
        client.getCatalogAgeRatings().map(AgeRatingEnum::toAgeRating)
    }

    override suspend fun getGenres(): Result<List<GenreModel>> = runCatching {
        client.getCatalogGenres().map(Genre::toGenreModel)
    }

    override suspend fun getProductionStatuses(): Result<List<ProductionStatus>> = runCatching {
        client.getCatalogProductionStatuses().map(ProductionStatusEnum::toProductionStatus)
    }

    override suspend fun getPublishStatuses(): Result<List<PublishStatus>> = runCatching {
        client.getCatalogPublishStatuses().map(PublishStatusEnum::toPublishStatus)
    }

    override suspend fun getSeasons(): Result<List<Season>> = runCatching {
        client.getCatalogSeasons().map(SeasonEnum::toSeason)
    }

    override suspend fun getSortingTypes(): Result<List<SortingType>> = runCatching {
        client.getCatalogSortingTypes().map(SortingTypeEnum::toSortingType)
    }

    override suspend fun getReleaseType(): Result<List<ReleaseType>> = runCatching {
        client.getCatalogReleaseTypes().map(ReleaseTypeEnum::toReleaseType)
    }

    override suspend fun getYears(): Result<List<Int>> = runCatching {
        client.getCatalogYears()
    }
}
