package com.xbot.network.api

import arrow.core.Either
import com.xbot.common.error.AppError
import com.xbot.network.models.dto.ReleaseDto
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Query

interface SearchApi {
    @GET("app/search/releases")
    suspend fun searchReleases(@Query("query") query: String): Either<AppError, List<ReleaseDto>>
}
