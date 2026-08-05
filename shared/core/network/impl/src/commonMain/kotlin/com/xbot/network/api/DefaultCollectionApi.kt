package com.xbot.network.api

import arrow.core.Either
import com.xbot.common.error.AppError
import com.xbot.network.client.HttpRequester
import com.xbot.network.client.requiresAuth
import com.xbot.network.models.dto.GenreDto
import com.xbot.network.models.dto.ReleaseDto
import com.xbot.network.models.enums.AgeRatingDto
import com.xbot.network.models.enums.CollectionTypeDto
import com.xbot.network.models.enums.ReleaseTypeDto
import com.xbot.network.models.responses.PaginatedResponse
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import org.koin.core.annotation.Singleton

@Singleton
internal class DefaultCollectionApi(private val requester: HttpRequester) : CollectionApi {
    override suspend fun getCollectionIds(): Either<AppError, Map<Int, CollectionTypeDto>> = requester.request {
        get("accounts/users/me/collections/ids") {
            requiresAuth()
        }
    }

    override suspend fun getCollectionReleases(
        page: Int,
        limit: Int,
        collectionType: CollectionTypeDto,
        genres: List<Int>?,
        types: List<ReleaseTypeDto>?,
        years: List<Int>?,
        search: String?,
        ageRatings: List<AgeRatingDto>?
    ): Either<AppError, PaginatedResponse<ReleaseDto>> = requester.request {
        get("accounts/users/me/collections/releases") {
            requiresAuth()
            parameter("page", page)
            parameter("limit", limit)
            parameter("type_of_collection", collectionType)
            genres?.let { parameter("f[genres]", it.joinToString(",")) }
            types?.let { parameter("f[types]", it.joinToString(",")) }
            years?.let { parameter("f[years]", it.joinToString(",")) }
            search?.let { parameter("f[search]", it) }
            ageRatings?.let { parameter("f[age_ratings]", it.joinToString(",")) }
        }
    }

    override suspend fun addToCollections(collections: Map<Int, CollectionTypeDto>): Either<AppError, Map<Int, CollectionTypeDto>> = requester.request {
        post("accounts/users/me/collections") {
            requiresAuth()
            setBody(collections.map { (releaseId, collectionType) ->
                mapOf(
                    "release_id" to releaseId,
                    "type_of_collection" to collectionType
                )
            })
        }
    }

    override suspend fun removeFromCollections(releaseIds: List<Int>): Either<AppError, Map<Int, CollectionTypeDto>> = requester.request {
        delete("accounts/users/me/collections") {
            requiresAuth()
            setBody(releaseIds.map { mapOf("release_id" to it) })
        }
    }

    override suspend fun getCollectionAgeRatings(): Either<AppError, List<AgeRatingDto>> = requester.request {
        get("accounts/users/me/collections/references/age-ratings") {
            requiresAuth()
        }
    }

    override suspend fun getCollectionGenres(): Either<AppError, List<GenreDto>> = requester.request {
        get("accounts/users/me/collections/references/genres") {
            requiresAuth()
        }
    }

    override suspend fun getCollectionReleaseTypes(): Either<AppError, List<ReleaseTypeDto>> = requester.request {
        get("accounts/users/me/collections/references/types") {
            requiresAuth()
        }
    }

    override suspend fun getCollectionYears(): Either<AppError, List<Int>> = requester.request {
        get("accounts/users/me/collections/references/years") {
            requiresAuth()
        }
    }
}
