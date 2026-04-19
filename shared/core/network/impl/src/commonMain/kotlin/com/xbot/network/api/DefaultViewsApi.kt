package com.xbot.network.api

import arrow.core.Either
import com.xbot.domain.models.DomainError
import com.xbot.network.client.ResilientHttpRequester
import com.xbot.network.client.requiresAuth
import com.xbot.network.models.dto.TimecodeApi
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import org.koin.core.annotation.Singleton

@Singleton
internal class DefaultViewsApi(private val requester: ResilientHttpRequester) : ViewsApi {
    override suspend fun getTimecodes(): Either<DomainError, List<TimecodeApi>> = requester.request {
        get("accounts/users/me/views/timecodes") {
            requiresAuth()
        }
    }

    override suspend fun updateTimecodes(
        timecodes: List<ViewsApi.UpdateTimecodesRequest>
    ): Either<DomainError, List<TimecodeApi>> = requester.request {
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
    ): Either<DomainError, List<TimecodeApi>> = requester.request {
        delete("accounts/users/me/views/timecodes") {
            requiresAuth()
            setBody(episodeIds.map { mapOf("release_episode_id" to it) })
        }
    }
}
