package com.xbot.network.requests.account

import com.xbot.network.client.AnilibriaClient
import com.xbot.network.models.entities.anime.GenreApi
import com.xbot.network.models.entities.anime.ReleaseApi
import com.xbot.network.models.enums.AgeRatingApi
import com.xbot.network.models.enums.ReleaseTypeApi
import com.xbot.network.models.enums.SortingTypeApi
import com.xbot.network.models.enums.FavoriteSortingTypeApi
import com.xbot.network.models.responses.common.PaginatedResponse
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

suspend fun AnilibriaClient.getFavoriteIds(): List<Int> = request {
    get("accounts/users/me/favorites/ids")
}

suspend fun AnilibriaClient.getFavoriteReleases(
    page: Int,
    limit: Int,
    years: List<Int>? = null,
    types: List<ReleaseTypeApi>? = null,
    genres: List<Int>? = null,
    search: String? = null,
    sorting: SortingTypeApi? = null,
    ageRatings: List<AgeRatingApi>? = null
): PaginatedResponse<ReleaseApi> = request {
    get("accounts/users/me/favorites/releases") {
        parameter("page", page)
        parameter("limit", limit)
        years?.let { parameter("f[years]", it.joinToString(",")) }
        types?.let { parameter("f[types]", it.joinToString(",")) }
        genres?.let { parameter("f[genres]", it.joinToString(",")) }
        search?.let { parameter("f[search]", it) }
        sorting?.let { parameter("f[sorting]", it) }
        ageRatings?.let { parameter("f[age_ratings]", it.joinToString(",")) }
    }
}

suspend fun AnilibriaClient.addToFavorites(
    releaseIds: List<Int>
): List<Int> = request {
    post("accounts/users/me/favorites") {
        contentType(ContentType.Application.Json)
        setBody(releaseIds.map { mapOf("release_id" to it) })
    }
}

suspend fun AnilibriaClient.removeFromFavorites(
    releaseIds: List<Int>
): List<Int> = request {
    delete("accounts/users/me/favorites") {
        contentType(ContentType.Application.Json)
        setBody(releaseIds.map { mapOf("release_id" to it) })
    }
}

suspend fun AnilibriaClient.getFavoriteAgeRatings(): List<AgeRatingApi> = request {
    get("accounts/users/me/favorites/references/age-ratings")
}

suspend fun AnilibriaClient.getFavoriteGenres(): List<GenreApi> = request {
    get("accounts/users/me/favorites/references/genres")
}

suspend fun AnilibriaClient.getFavoriteSortingTypes(): List<FavoriteSortingTypeApi> = request {
    get("accounts/users/me/favorites/references/sorting")
}

suspend fun AnilibriaClient.getFavoriteReleaseTypes(): List<ReleaseTypeApi> = request {
    get("accounts/users/me/favorites/references/types")
}

suspend fun AnilibriaClient.getFavoriteYears(): List<Int> = request {
    get("accounts/users/me/favorites/references/years")
}