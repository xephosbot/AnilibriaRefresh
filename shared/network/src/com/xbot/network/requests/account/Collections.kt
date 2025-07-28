package com.xbot.network.requests.account

import com.xbot.network.client.AnilibriaClient
import com.xbot.network.models.entities.anime.ReleaseApi
import com.xbot.network.models.entities.anime.GenreApi
import com.xbot.network.models.enums.AgeRatingApi
import com.xbot.network.models.enums.CollectionTypeApi
import com.xbot.network.models.enums.ReleaseTypeApi
import com.xbot.network.models.responses.common.PaginatedResponse
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

suspend fun AnilibriaClient.getCollectionIds(): Map<Int, CollectionTypeApi> = request {
    get("accounts/users/me/collections/ids")
}

suspend fun AnilibriaClient.getCollectionReleases(
    page: Int,
    limit: Int,
    collectionType: CollectionTypeApi,
    genres: List<Int>? = null,
    types: List<ReleaseTypeApi>? = null,
    years: List<Int>? = null,
    search: String? = null,
    ageRatings: List<AgeRatingApi>? = null
): PaginatedResponse<ReleaseApi> = request {
    get("accounts/users/me/collections/releases") {
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

suspend fun AnilibriaClient.addToCollections(
    collections: Map<Int, CollectionTypeApi> // releaseId to collectionType
): Map<Int, CollectionTypeApi> = request {
    post("accounts/users/me/collections") {
        contentType(ContentType.Application.Json)
        setBody(collections.map { (releaseId, collectionType) ->
            mapOf(
                "release_id" to releaseId,
                "type_of_collection" to collectionType
            )
        })
    }
}

suspend fun AnilibriaClient.removeFromCollections(
    releaseIds: List<Int>
): Map<Int, CollectionTypeApi> = request {
    delete("accounts/users/me/collections") {
        contentType(ContentType.Application.Json)
        setBody(releaseIds.map { mapOf("release_id" to it) })
    }
}

suspend fun AnilibriaClient.getCollectionAgeRatings(): List<AgeRatingApi> = request {
    get("accounts/users/me/collections/references/age-ratings")
}

suspend fun AnilibriaClient.getCollectionGenres(): List<GenreApi> = request {
    get("accounts/users/me/collections/references/genres")
}

suspend fun AnilibriaClient.getCollectionReleaseTypes(): List<ReleaseTypeApi> = request {
    get("accounts/users/me/collections/references/types")
}

suspend fun AnilibriaClient.getCollectionYears(): List<Int> = request {
    get("accounts/users/me/collections/references/years")
}