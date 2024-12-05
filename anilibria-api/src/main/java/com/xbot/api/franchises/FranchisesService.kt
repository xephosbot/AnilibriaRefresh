package com.xbot.api.franchises

import com.skydoves.sandwich.ApiResponse
import com.xbot.api.franchises.models.Franchise
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface FranchisesService {
    @GET("anime/franchises")
    suspend fun getFranchises(): ApiResponse<List<Franchise>>

    @GET("anime/franchises/{franchiseId}")
    suspend fun getFranchiseById(
        @Path("franchiseId") id: Int,
    ): ApiResponse<Franchise>

    @GET("anime/franchises/random")
    suspend fun getFranchisesRandom(
        @Query("limit") limit: Int,
    ): ApiResponse<List<Franchise>>

    @GET("anime/franchises/release/{releaseId}")
    suspend fun getFranchisesByRelease(
        @Path("releaseId") id: Int,
    ): ApiResponse<List<Franchise>>
}