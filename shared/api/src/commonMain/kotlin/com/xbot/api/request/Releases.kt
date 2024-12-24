package com.xbot.api.request

import com.xbot.api.client.AnilibriaClient
import com.xbot.api.models.releases.EpisodeWithRelease
import com.xbot.api.models.releases.ScheduleApi
import com.xbot.api.models.shared.ReleaseApi
import io.ktor.client.request.get
import io.ktor.client.request.parameter

suspend fun AnilibriaClient.getLatestReleases(limit: Int) = request<List<ReleaseApi>> {
    get("anime/releases/latest") {
        parameter("limit", limit)
    }
}

suspend fun AnilibriaClient.getRandomReleases(limit: Int) = request<List<ReleaseApi>> {
    get("anime/releases/random") {
        parameter("limit", limit)
    }
}

suspend fun AnilibriaClient.getRelease(id: Int) = request<ReleaseApi> {
    get("anime/releases/${id}")
}

//TODO:
suspend fun AnilibriaClient.getEpisode(releaseEpisodeId: Int) = request<EpisodeWithRelease> {
    get("anime/releases/episodes/${releaseEpisodeId}")
}

//TODO:
suspend fun AnilibriaClient.getScheduleNow() = request<Map<String, ScheduleApi>> {
    get("anime/schedule/now")
}

suspend fun AnilibriaClient.getScheduleWeek() = request<List<ScheduleApi>> {
    get("anime/schedule/week")
}