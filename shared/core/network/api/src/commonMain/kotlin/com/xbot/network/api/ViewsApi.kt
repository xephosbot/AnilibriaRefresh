package com.xbot.network.api

import arrow.core.Either
import com.xbot.domain.models.DomainError
import com.xbot.network.models.dto.TimecodeApi

interface ViewsApi {
    suspend fun getTimecodes(): Either<DomainError, List<TimecodeApi>>
    suspend fun updateTimecodes(timecodes: List<UpdateTimecodesRequest>): Either<DomainError, List<TimecodeApi>>
    suspend fun deleteTimecodes(episodeIds: List<String>): Either<DomainError, List<TimecodeApi>>

    data class UpdateTimecodesRequest(
        val episodeId: String,
        val time: Float,
        val isWatched: Boolean,
    )
}
