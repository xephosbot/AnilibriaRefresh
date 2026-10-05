package com.xbot.network.api

import arrow.core.Either
import com.xbot.common.error.AppError
import com.xbot.network.models.dto.FranchiseDto
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Path
import de.jensklingenberg.ktorfit.http.Query

interface FranchisesApi {
    @GET("anime/franchises")
    suspend fun getFranchises(): Either<AppError, List<FranchiseDto>>

    @GET("anime/franchises/{franchiseId}")
    suspend fun getFranchise(
        @Path("franchiseId") franchiseId: String
    ): Either<AppError, FranchiseDto>

    @GET("anime/franchises/random")
    suspend fun getFranchisesRandom(
        @Query("limit") limit: Int
    ): Either<AppError, List<FranchiseDto>>

    @GET("anime/franchises/release/{releaseId}")
    suspend fun getFranchisesByRelease(
        @Path("releaseId") releaseId: Int
    ): Either<AppError, List<FranchiseDto>>
}
