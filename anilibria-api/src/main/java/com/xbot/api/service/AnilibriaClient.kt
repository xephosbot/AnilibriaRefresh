package com.xbot.api.service

import com.skydoves.sandwich.ApiResponse
import com.xbot.api.models.Anime
import com.xbot.api.models.AnimeCatalogResponse
import javax.inject.Inject

class AnilibriaClient @Inject constructor(
    private val service: AnilibriaService
) {
    suspend fun getReleases(
        page: Int,
        limit: Int,
        genres: List<String>? = null,
        types: List<String>? = null,
        seasons: List<String>? = null,
        fromYear: Int? = null,
        toYear: Int? = null,
        search: String? = null,
        sorting: String? = null,
        ageRatings: List<String>? = null,
        publishStatuses: List<String>? = null,
        productionStatuses: List<String>? = null
    ): ApiResponse<AnimeCatalogResponse> {
        return service.getReleases(
            page = page,
            limit = limit,
            genres = genres?.joinToString(","),
            types = types?.joinToString(","),
            seasons = seasons?.joinToString(","),
            fromYear = fromYear,
            toYear = toYear,
            search = search,
            sorting = sorting,
            ageRatings = ageRatings?.joinToString(","),
            publishStatuses = publishStatuses?.joinToString(","),
            productionStatuses = productionStatuses?.joinToString(",")
        )
    }

    suspend fun getRelease(id: Int): ApiResponse<Anime> {
        return service.getRelease(id)
    }
}