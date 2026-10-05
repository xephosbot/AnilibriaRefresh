package com.xbot.network.api

import arrow.core.Either
import com.xbot.common.error.AppError
import com.xbot.network.models.dto.TimecodeApi
import com.xbot.network.models.requests.EpisodeIdRequest
import com.xbot.network.models.requests.TimecodeUpdateRequest
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.DELETE
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.POST

interface ViewsApi {
    @GET("accounts/users/me/views/timecodes")
    suspend fun getTimecodes(): Either<AppError, List<TimecodeApi>>

    @POST("accounts/users/me/views/timecodes")
    suspend fun updateTimecodes(
        @Body timecodes: List<TimecodeUpdateRequest>
    ): Either<AppError, Unit>

    @DELETE("accounts/users/me/views/timecodes")
    suspend fun deleteTimecodes(@Body episodeIds: List<EpisodeIdRequest>): Either<AppError, Unit>
}
