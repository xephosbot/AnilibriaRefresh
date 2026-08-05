package com.xbot.network.api

import arrow.core.Either
import com.xbot.common.error.AppError
import com.xbot.network.models.dto.TimecodeApi

interface ViewsApi {
    suspend fun getTimecodes(): Either<AppError, List<TimecodeApi>>
    suspend fun updateTimecodes(timecodes: List<UpdateTimecodesRequest>): Either<AppError, List<TimecodeApi>>
    suspend fun deleteTimecodes(episodeIds: List<String>): Either<AppError, List<TimecodeApi>>

    data class UpdateTimecodesRequest(
        val episodeId: String,
        val time: Float,
        val isWatched: Boolean,
    )
}
