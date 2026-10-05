package com.xbot.network.api

import arrow.core.Either
import com.xbot.common.error.AppError
import com.xbot.network.models.dto.EpisodeTimecodeDto
import com.xbot.network.models.dto.ReleaseDto
import com.xbot.network.models.dto.ReleaseMemberDto
import com.xbot.network.models.dto.ReleaseOwnRatingDto
import com.xbot.network.models.requests.ReleaseRatingRequest
import com.xbot.network.models.responses.PaginatedResponse
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.DELETE
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.Path
import de.jensklingenberg.ktorfit.http.Query

interface ReleasesApi {
    @GET("anime/releases/latest")
    suspend fun getLatestReleases(@Query("limit") limit: Int): Either<AppError, List<ReleaseDto>>

    @GET("anime/releases/random")
    suspend fun getRandomReleases(@Query("limit") limit: Int): Either<AppError, List<ReleaseDto>>

    @GET("anime/releases/list")
    suspend fun getReleasesList(
        @Query("ids[]") ids: List<Int>? = null,
        @Query("aliases[]") aliases: List<String>? = null,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 15
    ): Either<AppError, PaginatedResponse<ReleaseDto>>

    @GET("anime/releases/recommended")
    suspend fun getRecommendedReleases(
        @Query("limit") limit: Int? = null,
        @Query("release_id") releaseId: Int? = null
    ): Either<AppError, List<ReleaseDto>>

    @GET("anime/releases/{releaseId}/rating")
    suspend fun getOwnRating(
        @Path("releaseId") releaseId: Int
    ): Either<AppError, ReleaseOwnRatingDto>

    @POST("anime/releases/{releaseId}/rating")
    suspend fun rateRelease(
        @Path("releaseId") releaseId: Int,
        @Body request: ReleaseRatingRequest
    ): Either<AppError, ReleaseOwnRatingDto>

    @DELETE("anime/releases/{releaseId}/rating")
    suspend fun removeOwnRating(
        @Path("releaseId") releaseId: Int
    ): Either<AppError, ReleaseOwnRatingDto>

    @GET("anime/releases/{aliasOrId}")
    suspend fun getRelease(@Path("aliasOrId") aliasOrId: String): Either<AppError, ReleaseDto>

    @GET("anime/releases/{aliasOrId}/members")
    suspend fun getReleaseMembers(
        @Path("aliasOrId") aliasOrId: String
    ): Either<AppError, List<ReleaseMemberDto>>

    @GET("anime/releases/{aliasOrId}/episodes/timecodes")
    suspend fun getReleaseEpisodesTimecodes(
        @Path("aliasOrId") aliasOrId: String
    ): Either<AppError, List<EpisodeTimecodeDto>>
}
