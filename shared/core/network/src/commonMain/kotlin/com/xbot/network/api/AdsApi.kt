package com.xbot.network.api

import arrow.core.Either
import com.xbot.common.error.AppError
import com.xbot.network.models.dto.VastDto
import de.jensklingenberg.ktorfit.http.GET

interface AdsApi {
    @GET("media/vasts")
    suspend fun getVasts(): Either<AppError, List<VastDto>>

    @GET("media/manifest.xml")
    suspend fun getVastsChain(): Either<AppError, String>
}
