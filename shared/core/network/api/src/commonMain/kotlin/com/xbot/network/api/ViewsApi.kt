package com.xbot.network.api

import arrow.core.Either
import com.xbot.network.client.NetworkError
import com.xbot.network.models.dto.TimecodeApi

interface ViewsApi {
    suspend fun getTimecodes(): Either<NetworkError, List<TimecodeApi>>
    suspend fun updateTimecodes(timecodes: List<UpdateTimecodesRequest>): Either<NetworkError, List<TimecodeApi>>
    suspend fun deleteTimecodes(episodeIds: List<String>): Either<NetworkError, List<TimecodeApi>>

    data class UpdateTimecodesRequest(
        val episodeId: String,
        val time: Float,
        val isWatched: Boolean,
    )
}
