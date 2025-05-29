package com.xbot.data.repository

import com.xbot.network.client.AnilibriaClient
import com.xbot.network.models.entities.anime.GenreApi
import com.xbot.network.models.enums.AgeRatingApi
import com.xbot.network.models.enums.ProductionStatusApi
import com.xbot.network.models.enums.PublishStatusApi
import com.xbot.network.models.enums.ReleaseTypeApi
import com.xbot.network.models.enums.SeasonApi
import com.xbot.network.models.enums.SortingTypeApi
import com.xbot.network.requests.anime.getCatalogAgeRatings
import com.xbot.network.requests.anime.getCatalogGenres
import com.xbot.network.requests.anime.getCatalogProductionStatuses
import com.xbot.network.requests.anime.getCatalogPublishStatuses
import com.xbot.network.requests.anime.getCatalogReleaseTypes
import com.xbot.network.requests.anime.getCatalogSeasons
import com.xbot.network.requests.anime.getCatalogSortingTypes
import com.xbot.network.requests.anime.getCatalogYears
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
