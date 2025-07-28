package com.xbot.network.requests.account

import com.xbot.network.client.AnilibriaClient
import com.xbot.network.models.entities.accounts.TimecodeApi
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

suspend fun AnilibriaClient.getTimecodes(): List<TimecodeApi> = request {
    get("accounts/users/me/views/timecodes")
}

suspend fun AnilibriaClient.updateTimecodes(
    timecodes: List<Triple<String, Float, Boolean>> // episodeId, time, isWatched
): List<TimecodeApi> = request {
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

suspend fun AnilibriaClient.deleteTimecodes(
    episodeIds: List<String>
): List<TimecodeApi> = request {
    delete("accounts/users/me/views/timecodes") {
        contentType(ContentType.Application.Json)
        setBody(episodeIds.map { mapOf("release_episode_id" to it) })
    }
}