package com.xbot.api.request

import com.xbot.api.client.AnilibriaClient
import com.xbot.api.models.releases.EpisodeWithRelease
import com.xbot.api.models.releases.Schedule
import com.xbot.api.models.shared.Release
import io.ktor.client.request.get
import io.ktor.client.request.parameter

suspend fun AnilibriaClient.getLatestReleases(limit: Int) = request<List<Release>> {
    get("anime/releases/latest") {
        parameter("limit", limit)
    }
}

suspend fun AnilibriaClient.getRandomReleases(limit: Int) = request<List<Release>> {
    get("anime/releases/random") {
        parameter("limit", limit)
    }
}

suspend fun AnilibriaClient.getRelease(id: Int) = request<Release> {
    get("anime/releases/${id}")
}

//TODO:
suspend fun AnilibriaClient.getEpisode(releaseEpisodeId: Int) = request<EpisodeWithRelease> {
    get("anime/releases/episodes/${releaseEpisodeId}")
}

//TODO:
suspend fun AnilibriaClient.getScheduleNow() = request<Map<String, Schedule>> {
    get("anime/schedule/now")
}

suspend fun AnilibriaClient.getScheduleWeek() = request<List<Schedule>> {
    get("anime/schedule/week")
}