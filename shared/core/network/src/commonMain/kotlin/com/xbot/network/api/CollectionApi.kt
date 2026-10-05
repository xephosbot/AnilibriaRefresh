package com.xbot.network.api

import arrow.core.Either
import com.xbot.common.error.AppError
import com.xbot.network.models.dto.GenreDto
import com.xbot.network.models.dto.ReleaseDto
import com.xbot.network.models.enums.AgeRatingDto
import com.xbot.network.models.enums.CollectionTypeDto
import com.xbot.network.models.enums.ReleaseTypeDto
import com.xbot.network.models.requests.CollectionRequest
import com.xbot.network.models.requests.ReleaseIdRequest
import com.xbot.network.models.responses.PaginatedResponse
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.DELETE
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.Query

interface CollectionApi {
    @GET("accounts/users/me/collections/ids")
    suspend fun getCollectionIds(): Either<AppError, Map<Int, CollectionTypeDto>>

    @GET("accounts/users/me/collections/releases")
    suspend fun getCollectionReleases(
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("type_of_collection") collectionType: CollectionTypeDto,
        @Query("f[genres][]") genres: List<Int>? = null,
        @Query("f[types][]") types: List<ReleaseTypeDto>? = null,
        @Query("f[years][]") years: List<Int>? = null,
        @Query("f[search]") search: String? = null,
        @Query("f[age_ratings][]") ageRatings: List<AgeRatingDto>? = null
    ): Either<AppError, PaginatedResponse<ReleaseDto>>

    @POST("accounts/users/me/collections")
    suspend fun addToCollections(
        @Body collections: List<CollectionRequest>
    ): Either<AppError, Map<Int, CollectionTypeDto>>

    @DELETE("accounts/users/me/collections")
    suspend fun removeFromCollections(
        @Body releaseIds: List<ReleaseIdRequest>
    ): Either<AppError, Map<Int, CollectionTypeDto>>

    @GET("accounts/users/me/collections/references/age-ratings")
    suspend fun getCollectionAgeRatings(): Either<AppError, List<AgeRatingDto>>

    @GET("accounts/users/me/collections/references/genres")
    suspend fun getCollectionGenres(): Either<AppError, List<GenreDto>>

    @GET("accounts/users/me/collections/references/types")
    suspend fun getCollectionReleaseTypes(): Either<AppError, List<ReleaseTypeDto>>

    @GET("accounts/users/me/collections/references/years")
    suspend fun getCollectionYears(): Either<AppError, List<Int>>
}
