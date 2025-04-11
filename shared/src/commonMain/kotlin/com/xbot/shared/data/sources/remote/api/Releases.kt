package com.xbot.shared.data.sources.remote.api

import com.xbot.shared.data.sources.remote.AnilibriaClient
import com.xbot.shared.data.sources.remote.models.releases.EpisodeWithRelease
import com.xbot.shared.data.sources.remote.models.releases.ScheduleApi
import com.xbot.shared.data.sources.remote.models.shared.ReleaseApi
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

suspend fun AnilibriaClient.getRelease(aliasOrId: String) = request<ReleaseApi> {
    get("anime/releases/${aliasOrId}")
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