package com.xbot.network.requests.media

import com.xbot.network.client.AnilibriaClient
import com.xbot.network.models.entities.media.VideoApi
import io.ktor.client.request.get
import io.ktor.client.request.parameter

suspend fun AnilibriaClient.getVideos(
    limit: Int
): List<VideoApi> = request {
    get("media/videos") {
        parameter("limit", limit)
    }
}