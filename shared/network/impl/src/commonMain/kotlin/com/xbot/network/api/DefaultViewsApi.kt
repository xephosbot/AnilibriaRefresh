package com.xbot.network.api

import arrow.core.Either
import com.xbot.network.client.NetworkError
import com.xbot.network.client.request
import com.xbot.network.client.requiresAuth
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import com.xbot.network.models.dto.*
import com.xbot.network.models.enums.*
import com.xbot.network.models.responses.*
import org.koin.core.annotation.Singleton

@Singleton
internal class DefaultViewsApi(private val client: HttpClient) : ViewsApi {
    override suspend fun getTimecodes(): Either<NetworkError, List<TimecodeApi>> = client.request {
        get("accounts/users/me/views/timecodes") {
            requiresAuth()
        }
    }

    override suspend fun updateTimecodes(
        timecodes: List<ViewsApi.UpdateTimecodesRequest>
    ): Either<NetworkError, List<TimecodeApi>> = client.request {
        post("accounts/users/me/views/timecodes") {
            requiresAuth()
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
            requiresAuth()
            setBody(episodeIds.map { mapOf("release_episode_id" to it) })
        }
    }
}
