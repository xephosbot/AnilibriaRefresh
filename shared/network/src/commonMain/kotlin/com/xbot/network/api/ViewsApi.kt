package com.xbot.network.api

import arrow.core.Either
import com.xbot.network.client.NetworkError
import com.xbot.network.client.request
import com.xbot.network.models.dto.TimecodeApi
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*

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

internal class DefaultViewsApi(private val client: HttpClient) : ViewsApi {
    override suspend fun getTimecodes(): Either<NetworkError, List<TimecodeApi>> = client.request {
        get("accounts/users/me/views/timecodes")
    }

    override suspend fun updateTimecodes(
        timecodes: List<ViewsApi.UpdateTimecodesRequest>
    ): Either<NetworkError, List<TimecodeApi>> = client.request {
        post("accounts/users/me/views/timecodes") {
            contentType(ContentType.Application.Json)
            setBody(timecodes.map { (episodeId, time, isWatched) ->
                mapOf(
                    "release_episode_id" to episodeId,
                    "time" to time,
                    "is_watched" to isWatched
                )
            })
        }
    }

    override suspend fun deleteTimecodes(
        episodeIds: List<String>
    ): Either<NetworkError, List<TimecodeApi>> = client.request {
        delete("accounts/users/me/views/timecodes") {
            contentType(ContentType.Application.Json)
            setBody(episodeIds.map { mapOf("release_episode_id" to it) })
        }
    }
}