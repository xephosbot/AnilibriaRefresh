package com.xbot.network.api

import arrow.core.Either
import com.xbot.common.error.AppError
import com.xbot.network.models.dto.AppStatusDto
import de.jensklingenberg.ktorfit.http.GET

interface AppApi {
    @GET("app/status")
    suspend fun getStatus(): Either<AppError, AppStatusDto>
}
