package com.xbot.api.releases

import com.skydoves.sandwich.ApiResponse
import com.xbot.api.shared.Release
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ReleasesService {
    @GET("anime/releases/latest")
    suspend fun getLatestReleases(
        @Query("limit") limit: Int,
    ): ApiResponse<List<Release>>

    @GET("anime/releases/random")
    suspend fun getRandomReleases(
        @Query("limit") limit: Int,
    ): ApiResponse<List<Release>>

    @GET("anime/releases/{id}")
    suspend fun getRelease(
        @Path("id") id: Int,
    ): ApiResponse<Release>
}