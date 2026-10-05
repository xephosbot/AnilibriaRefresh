package com.xbot.network.api

import arrow.core.Either
import com.xbot.common.error.AppError
import com.xbot.network.models.dto.GenreDto
import com.xbot.network.models.dto.ReleaseDto
import com.xbot.network.models.enums.AgeRatingDto
import com.xbot.network.models.enums.FavoriteSortingTypeDto
import com.xbot.network.models.enums.ReleaseTypeDto
import com.xbot.network.models.enums.SortingTypeDto
import com.xbot.network.models.requests.ReleaseIdRequest
import com.xbot.network.models.responses.PaginatedResponse
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.DELETE
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.Query

interface FavoritesApi {
    @GET("accounts/users/me/favorites/ids")
    suspend fun getFavoriteIds(): Either<AppError, List<Int>>

    @GET("accounts/users/me/favorites/releases")
    suspend fun getFavoriteReleases(
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("f[years][]") years: List<Int>? = null,
        @Query("f[types][]") types: List<ReleaseTypeDto>? = null,
        @Query("f[genres][]") genres: List<Int>? = null,
        @Query("f[search]") search: String? = null,
        @Query("f[sorting]") sorting: SortingTypeDto? = null,
        @Query("f[age_ratings][]") ageRatings: List<AgeRatingDto>? = null
    ): Either<AppError, PaginatedResponse<ReleaseDto>>

    @POST("accounts/users/me/favorites")
    suspend fun addToFavorites(
        @Body releaseIds: List<ReleaseIdRequest>
    ): Either<AppError, List<Int>>

    @DELETE("accounts/users/me/favorites")
    suspend fun removeFromFavorites(
        @Body releaseIds: List<ReleaseIdRequest>
    ): Either<AppError, List<Int>>

    @GET("accounts/users/me/favorites/references/age-ratings")
    suspend fun getFavoriteAgeRatings(): Either<AppError, List<AgeRatingDto>>

    @GET("accounts/users/me/favorites/references/genres")
    suspend fun getFavoriteGenres(): Either<AppError, List<GenreDto>>

    @GET("accounts/users/me/favorites/references/sorting")
    suspend fun getFavoriteSortingTypes(): Either<AppError, List<FavoriteSortingTypeDto>>

    @GET("accounts/users/me/favorites/references/types")
    suspend fun getFavoriteReleaseTypes(): Either<AppError, List<ReleaseTypeDto>>

    @GET("accounts/users/me/favorites/references/years")
    suspend fun getFavoriteYears(): Either<AppError, List<Int>>
}
