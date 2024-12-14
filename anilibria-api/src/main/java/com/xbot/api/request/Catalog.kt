package com.xbot.api.request

import com.xbot.api.client.AnilibriaClient
import com.xbot.api.models.shared.Genre
import com.xbot.api.models.shared.LabeledValue
import com.xbot.api.models.shared.ReleasesWithMetadata
import com.xbot.api.models.shared.enums.AgeRatingEnum
import com.xbot.api.models.shared.enums.ProductionStatusEnum
import com.xbot.api.models.shared.enums.PublishStatusEnum
import com.xbot.api.models.shared.enums.ReleaseTypeEnum
import com.xbot.api.models.shared.enums.SeasonEnum
import com.xbot.api.models.shared.enums.SortingTypeEnum
import io.ktor.client.request.get
import io.ktor.client.request.parameter

suspend fun AnilibriaClient.getCatalogReleases(
    page: Int,
    limit: Int,
    genres: List<Int>? = null,
    types: List<ReleaseTypeEnum>? = null,
    seasons: List<SeasonEnum>? = null,
    fromYear: Int? = null,
    toYear: Int? = null,
    search: String? = null,
    sorting: SortingTypeEnum? = null,
    ageRatings: List<AgeRatingEnum>? = null,
    publishStatuses: List<PublishStatusEnum>? = null,
    productionStatuses: List<ProductionStatusEnum>? = null,
): ReleasesWithMetadata = request {
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

suspend fun AnilibriaClient.getCatalogAgeRatings() = request<List<LabeledValue<AgeRatingEnum>>> {
    get("anime/catalog/references/age-ratings")
}.map(com.xbot.api.models.shared.LabeledValue<AgeRatingEnum>::value)

suspend fun AnilibriaClient.getCatalogGenres(): List<Genre> = request {
    get("anime/catalog/references/genres")
}

suspend fun AnilibriaClient.getCatalogProductionStatuses() = request<List<LabeledValue<ProductionStatusEnum>>> {
    get("anime/catalog/references/production-statuses")
}.map(LabeledValue<ProductionStatusEnum>::value)

suspend fun AnilibriaClient.getCatalogPublishStatuses() = request<List<LabeledValue<PublishStatusEnum>>> {
    get("anime/catalog/references/publish-statuses")
}.map(LabeledValue<PublishStatusEnum>::value)

suspend fun AnilibriaClient.getCatalogSeasons() = request<List<LabeledValue<SeasonEnum>>> {
    get("anime/catalog/references/seasons")
}.map(LabeledValue<SeasonEnum>::value)

suspend fun AnilibriaClient.getCatalogSortingTypes() = request<List<LabeledValue<SortingTypeEnum>>> {
    get("anime/catalog/references/sorting")
}.map(LabeledValue<SortingTypeEnum>::value)

suspend fun AnilibriaClient.getCatalogReleaseTypes() = request<List<LabeledValue<ReleaseTypeEnum>>> {
    get("anime/catalog/references/types")
}.map(LabeledValue<ReleaseTypeEnum>::value)

suspend fun AnilibriaClient.getCatalogYears(): List<Int> = request {
    get("anime/catalog/references/years")
}