package com.xbot.network.requests.anime

import com.xbot.network.client.AnilibriaClient
import com.xbot.network.models.entities.anime.ReleaseApi
import com.xbot.network.models.entities.anime.GenreApi
import com.xbot.network.models.enums.AgeRatingApi
import com.xbot.network.models.enums.ProductionStatusApi
import com.xbot.network.models.enums.PublishStatusApi
import com.xbot.network.models.enums.ReleaseTypeApi
import com.xbot.network.models.enums.SeasonApi
import com.xbot.network.models.enums.SortingTypeApi
import com.xbot.network.models.responses.common.PaginatedResponse
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
): PaginatedResponse<ReleaseApi> = request {
    get("anime/catalog/releases") {
        parameter("page", page)
        parameter("limit", limit)
        genres?.let { parameter("f[genres]", it.joinToString(",")) }
        types?.let { parameter("f[types]", it.joinToString(",")) }
        seasons?.let { parameter("f[seasons]", it.joinToString(",")) }
        fromYear?.let { parameter("f[years][from_year]", it) }
        toYear?.let { parameter("f[years][to_year]", it) }
        search?.let { parameter("f[search]", it) }
        sorting?.let { parameter("f[sorting]", it) }
        ageRatings?.let { parameter("f[age_ratings]", it.joinToString(",")) }
        publishStatuses?.let { parameter("f[publish_statuses]", it.joinToString(",")) }
        productionStatuses?.let { parameter("f[production_statuses]", it.joinToString(",")) }
    }
}

suspend fun AnilibriaClient.getCatalogAgeRatings(): List<AgeRatingApi> = request {
    get("anime/catalog/references/age-ratings")
}

suspend fun AnilibriaClient.getCatalogGenres(): List<GenreApi> = request {
    get("anime/catalog/references/genres")
}

suspend fun AnilibriaClient.getCatalogProductionStatuses(): List<ProductionStatusApi> = request {
    get("anime/catalog/references/production-statuses")
}

suspend fun AnilibriaClient.getCatalogPublishStatuses(): List<PublishStatusApi> = request {
    get("anime/catalog/references/publish-statuses")
}

suspend fun AnilibriaClient.getCatalogSeasons(): List<SeasonApi> = request {
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