package com.xbot.shared.data.sources.remote.api

import com.xbot.shared.data.sources.remote.AnilibriaClient
import com.xbot.shared.data.sources.remote.models.shared.GenreApi
import com.xbot.shared.data.sources.remote.models.shared.ReleasesWithMetadataApi
import com.xbot.shared.data.sources.remote.models.shared.enums.AgeRatingApi
import com.xbot.shared.data.sources.remote.models.shared.enums.ProductionStatusApi
import com.xbot.shared.data.sources.remote.models.shared.enums.PublishStatusApi
import com.xbot.shared.data.sources.remote.models.shared.enums.ReleaseTypeApi
import com.xbot.shared.data.sources.remote.models.shared.enums.SeasonApi
import com.xbot.shared.data.sources.remote.models.shared.enums.SortingTypeApi
import io.ktor.client.request.get
import io.ktor.client.request.parameter

suspend fun AnilibriaClient.getCatalogReleases(
    page: Int,
    limit: Int,
    genres: List<Int>? = null,
    types: List<ReleaseTypeApi>? = null,
    seasons: List<SeasonApi>? = null,
    fromYear: Int? = null,
    toYear: Int? = null,
    search: String? = null,
    sorting: SortingTypeApi? = null,
    ageRatings: List<AgeRatingApi>? = null,
    publishStatuses: List<PublishStatusApi>? = null,
    productionStatuses: List<ProductionStatusApi>? = null,
) = request<ReleasesWithMetadataApi> {
    get("anime/catalog/releases") {
        parameter("page", page)
        parameter("limit", limit)
        parameter("f[genres]", genres?.joinToString(","))
        parameter("f[types]", types?.joinToString(","))
        parameter("f[seasons]", seasons?.joinToString(","))
        parameter("f[years][from_year]", fromYear)
        parameter("f[years][to_year]", toYear)
        parameter("f[search]", search)
        parameter("f[sorting]", sorting)
        parameter("f[age_ratings]", ageRatings?.joinToString(","))
        parameter("f[publish_statuses]", publishStatuses?.joinToString(","))
        parameter("f[production_statuses]", productionStatuses?.joinToString(","))
    }
}

suspend fun AnilibriaClient.getCatalogAgeRatings() = request<List<AgeRatingApi>> {
    get("anime/catalog/references/age-ratings")
}

suspend fun AnilibriaClient.getCatalogGenres() = request<List<GenreApi>> {
    get("anime/catalog/references/genres")
}

suspend fun AnilibriaClient.getCatalogProductionStatuses() = request<List<ProductionStatusApi>> {
    get("anime/catalog/references/production-statuses")
}

suspend fun AnilibriaClient.getCatalogPublishStatuses() = request<List<PublishStatusApi>> {
    get("anime/catalog/references/publish-statuses")
}

suspend fun AnilibriaClient.getCatalogSeasons() = request<List<SeasonApi>> {
    get("anime/catalog/references/seasons")
}

suspend fun AnilibriaClient.getCatalogSortingTypes() = request<List<SortingTypeApi>> {
    get("anime/catalog/references/sorting")
}

suspend fun AnilibriaClient.getCatalogReleaseTypes() = request<List<ReleaseTypeApi>> {
    get("anime/catalog/references/types")
}

suspend fun AnilibriaClient.getCatalogYears() = request<List<Int>> {
    get("anime/catalog/references/years")
}