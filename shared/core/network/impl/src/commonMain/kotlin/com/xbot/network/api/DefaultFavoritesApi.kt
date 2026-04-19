package com.xbot.network.api

import arrow.core.Either
import com.xbot.domain.models.DomainError
import com.xbot.network.client.ResilientHttpRequester
import com.xbot.network.client.requiresAuth
import com.xbot.network.models.dto.GenreDto
import com.xbot.network.models.dto.ReleaseDto
import com.xbot.network.models.enums.AgeRatingDto
import com.xbot.network.models.enums.FavoriteSortingTypeDto
import com.xbot.network.models.enums.ReleaseTypeDto
import com.xbot.network.models.enums.SortingTypeDto
import com.xbot.network.models.responses.PaginatedResponse
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import org.koin.core.annotation.Singleton

@Singleton
internal class DefaultFavoritesApi(private val requester: ResilientHttpRequester) : FavoritesApi {
    override suspend fun getFavoriteIds(): Either<DomainError, List<Int>> = requester.request {
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
    ): Either<DomainError, PaginatedResponse<ReleaseDto>> = requester.request {
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
    ): Either<DomainError, List<Int>> = requester.request {
        post("accounts/users/me/favorites") {
            requiresAuth()
            setBody(releaseIds.map { mapOf("release_id" to it) })
        }
    }

    override suspend fun removeFromFavorites(
        releaseIds: List<Int>
    ): Either<DomainError, List<Int>> = requester.request {
        delete("accounts/users/me/favorites") {
            requiresAuth()
            setBody(releaseIds.map { mapOf("release_id" to it) })
        }
    }

    override suspend fun getFavoriteAgeRatings(): Either<DomainError, List<AgeRatingDto>> = requester.request {
        get("accounts/users/me/favorites/references/age-ratings") {
            requiresAuth()
        }
    }

    override suspend fun getFavoriteGenres(): Either<DomainError, List<GenreDto>> = requester.request {
        get("accounts/users/me/favorites/references/genres") {
            requiresAuth()
        }
    }

    override suspend fun getFavoriteSortingTypes(): Either<DomainError, List<FavoriteSortingTypeDto>> = requester.request {
        get("accounts/users/me/favorites/references/sorting") {
            requiresAuth()
        }
    }

    override suspend fun getFavoriteReleaseTypes(): Either<DomainError, List<ReleaseTypeDto>> = requester.request {
        get("accounts/users/me/favorites/references/types") {
            requiresAuth()
        }
    }

    override suspend fun getFavoriteYears(): Either<DomainError, List<Int>> = requester.request {
        get("accounts/users/me/favorites/references/years") {
            requiresAuth()
        }
    }
}
