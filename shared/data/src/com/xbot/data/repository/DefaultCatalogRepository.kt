package com.xbot.data.repository

import com.xbot.api.client.AnilibriaClient
import com.xbot.api.models.shared.GenreApi
import com.xbot.api.models.shared.enums.AgeRatingApi
import com.xbot.api.models.shared.enums.ProductionStatusApi
import com.xbot.api.models.shared.enums.PublishStatusApi
import com.xbot.api.models.shared.enums.ReleaseTypeApi
import com.xbot.api.models.shared.enums.SeasonApi
import com.xbot.api.models.shared.enums.SortingTypeApi
import com.xbot.api.request.getCatalogAgeRatings
import com.xbot.api.request.getCatalogGenres
import com.xbot.api.request.getCatalogProductionStatuses
import com.xbot.api.request.getCatalogPublishStatuses
import com.xbot.api.request.getCatalogReleaseTypes
import com.xbot.api.request.getCatalogSeasons
import com.xbot.api.request.getCatalogSortingTypes
import com.xbot.api.request.getCatalogYears
import com.xbot.data.mapper.toDomain
import com.xbot.domain.models.Genre
import com.xbot.domain.models.enums.AgeRating
import com.xbot.domain.models.enums.ProductionStatus
import com.xbot.domain.models.enums.PublishStatus
import com.xbot.domain.models.enums.ReleaseType
import com.xbot.domain.models.enums.Season
import com.xbot.domain.models.enums.SortingType
import com.xbot.domain.repository.CatalogRepository

internal class DefaultCatalogRepository(
    private val client: AnilibriaClient
) : CatalogRepository {
    override suspend fun getAgeRatings(): Result<List<AgeRating>> = runCatching {
        client.getCatalogAgeRatings().map(AgeRatingApi::toDomain)
    }

    override suspend fun getGenres(): Result<List<Genre>> = runCatching {
        client.getCatalogGenres().map(GenreApi::toDomain)
    }

    override suspend fun getProductionStatuses(): Result<List<ProductionStatus>> = runCatching {
        client.getCatalogProductionStatuses().map(ProductionStatusApi::toDomain)
    }

    override suspend fun getPublishStatuses(): Result<List<PublishStatus>> = runCatching {
        client.getCatalogPublishStatuses().map(PublishStatusApi::toDomain)
    }

    override suspend fun getSeasons(): Result<List<Season>> = runCatching {
        client.getCatalogSeasons().map(SeasonApi::toDomain)
    }

    override suspend fun getSortingTypes(): Result<List<SortingType>> = runCatching {
        client.getCatalogSortingTypes().map(SortingTypeApi::toDomain)
    }

    override suspend fun getReleaseTypes(): Result<List<ReleaseType>> = runCatching {
        client.getCatalogReleaseTypes().map(ReleaseTypeApi::toDomain)
    }

    override suspend fun getYears(): Result<ClosedRange<Int>> = runCatching {
        client.getCatalogYears().let { years ->
            years.first()..years.last()
        }
    }
}
