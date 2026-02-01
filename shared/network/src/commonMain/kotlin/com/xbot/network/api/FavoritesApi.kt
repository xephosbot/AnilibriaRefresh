package com.xbot.network.api

import arrow.core.Either
import com.xbot.network.client.NetworkError
import com.xbot.network.client.request
import com.xbot.network.client.requiresAuth
import com.xbot.network.models.dto.GenreDto
import com.xbot.network.models.dto.ReleaseDto
import com.xbot.network.models.enums.AgeRatingDto
import com.xbot.network.models.enums.FavoriteSortingTypeDto
import com.xbot.network.models.enums.ReleaseTypeDto
import com.xbot.network.models.enums.SortingTypeDto
import com.xbot.network.models.responses.PaginatedResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody

interface FavoritesApi {
    suspend fun getFavoriteIds(): Either<NetworkError, List<Int>>
    suspend fun getFavoriteReleases(
        page: Int,
        limit: Int,
        years: List<Int>? = null,
        types: List<ReleaseTypeDto>? = null,
        genres: List<Int>? = null,
        search: String? = null,
        sorting: SortingTypeDto? = null,
        ageRatings: List<AgeRatingDto>? = null
    ): Either<NetworkError, PaginatedResponse<ReleaseDto>>
    suspend fun addToFavorites(releaseIds: List<Int>): Either<NetworkError, List<Int>>
    suspend fun removeFromFavorites(releaseIds: List<Int>): Either<NetworkError, List<Int>>
    suspend fun getFavoriteAgeRatings(): Either<NetworkError, List<AgeRatingDto>>
    suspend fun getFavoriteGenres(): Either<NetworkError, List<GenreDto>>
    suspend fun getFavoriteSortingTypes(): Either<NetworkError, List<FavoriteSortingTypeDto>>
    suspend fun getFavoriteReleaseTypes(): Either<NetworkError, List<ReleaseTypeDto>>
    suspend fun getFavoriteYears(): Either<NetworkError, List<Int>>
}

internal class DefaultFavoritesApi(private val client: HttpClient) : FavoritesApi {
    override suspend fun getFavoriteIds(): Either<NetworkError, List<Int>> = client.request {
        get("accounts/users/me/favorites/ids") {
            requiresAuth()
        }
    }

    override suspend fun getFavoriteReleases(
        page: Int,
        limit: Int,
        years: List<Int>?,
        types: List<ReleaseTypeDto>?,
        genres: List<Int>?,
        search: String?,
        sorting: SortingTypeDto?,
        ageRatings: List<AgeRatingDto>?
    ): Either<NetworkError, PaginatedResponse<ReleaseDto>> = client.request {
        get("accounts/users/me/favorites/releases") {
            requiresAuth()
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

    override suspend fun addToFavorites(
        releaseIds: List<Int>
    ): Either<NetworkError, List<Int>> = client.request {
        post("accounts/users/me/favorites") {
            requiresAuth()
            setBody(releaseIds.map { mapOf("release_id" to it) })
        }
    }

    override suspend fun removeFromFavorites(
        releaseIds: List<Int>
    ): Either<NetworkError, List<Int>> = client.request {
        delete("accounts/users/me/favorites") {
            requiresAuth()
            setBody(releaseIds.map { mapOf("release_id" to it) })
        }
    }

    override suspend fun getFavoriteAgeRatings(): Either<NetworkError, List<AgeRatingDto>> = client.request {
        get("accounts/users/me/favorites/references/age-ratings") {
            requiresAuth()
        }
    }

    override suspend fun getFavoriteGenres(): Either<NetworkError, List<GenreDto>> = client.request {
        get("accounts/users/me/favorites/references/genres") {
            requiresAuth()
        }
    }

    override suspend fun getFavoriteSortingTypes(): Either<NetworkError, List<FavoriteSortingTypeDto>> = client.request {
        get("accounts/users/me/favorites/references/sorting") {
            requiresAuth()
        }
    }

    override suspend fun getFavoriteReleaseTypes(): Either<NetworkError, List<ReleaseTypeDto>> = client.request {
        get("accounts/users/me/favorites/references/types") {
            requiresAuth()
        }
    }

    override suspend fun getFavoriteYears(): Either<NetworkError, List<Int>> = client.request {
        get("accounts/users/me/favorites/references/years") {
            requiresAuth()
        }
    }
}