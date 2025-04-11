package com.xbot.shared.data.repositories

import com.xbot.shared.data.mapper.toDomain
import com.xbot.shared.data.sources.remote.AnilibriaClient
import com.xbot.shared.data.sources.remote.api.getCatalogAgeRatings
import com.xbot.shared.data.sources.remote.api.getCatalogGenres
import com.xbot.shared.data.sources.remote.api.getCatalogProductionStatuses
import com.xbot.shared.data.sources.remote.api.getCatalogPublishStatuses
import com.xbot.shared.data.sources.remote.api.getCatalogReleaseTypes
import com.xbot.shared.data.sources.remote.api.getCatalogSeasons
import com.xbot.shared.data.sources.remote.api.getCatalogSortingTypes
import com.xbot.shared.data.sources.remote.api.getCatalogYears
import com.xbot.shared.data.sources.remote.models.shared.GenreApi
import com.xbot.shared.data.sources.remote.models.shared.enums.AgeRatingApi
import com.xbot.shared.data.sources.remote.models.shared.enums.ProductionStatusApi
import com.xbot.shared.data.sources.remote.models.shared.enums.PublishStatusApi
import com.xbot.shared.data.sources.remote.models.shared.enums.ReleaseTypeApi
import com.xbot.shared.data.sources.remote.models.shared.enums.SeasonApi
import com.xbot.shared.data.sources.remote.models.shared.enums.SortingTypeApi
import com.xbot.shared.domain.models.Genre
import com.xbot.shared.domain.models.enums.AgeRating
import com.xbot.shared.domain.models.enums.ProductionStatus
import com.xbot.shared.domain.models.enums.PublishStatus
import com.xbot.shared.domain.models.enums.ReleaseType
import com.xbot.shared.domain.models.enums.Season
import com.xbot.shared.domain.models.enums.SortingType
import com.xbot.shared.domain.repository.CatalogRepository

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
