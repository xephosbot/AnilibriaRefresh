package com.xbot.network.requests.anime

import com.xbot.network.client.AnilibriaClient
import com.xbot.network.models.entities.anime.EpisodeWithRelease
import com.xbot.network.models.entities.anime.ScheduleApi
import com.xbot.network.models.entities.anime.ReleaseApi
import com.xbot.network.models.entities.anime.ReleaseMemberApi
import com.xbot.network.models.responses.common.PaginatedResponse
import io.ktor.client.request.get
import io.ktor.client.request.parameter

suspend fun AnilibriaClient.getLatestReleases(
    limit: Int
): List<ReleaseApi> = request {
    get("anime/releases/latest") {
        parameter("limit", limit)
    }
}

suspend fun AnilibriaClient.getRandomReleases(
    limit: Int
): List<ReleaseApi> = request {
    get("anime/releases/random") {
        parameter("limit", limit)
    }
}

suspend fun AnilibriaClient.getReleasesList(
    ids: List<Int>? = null,
    aliases: List<String>? = null,
    page: Int = 1,
    limit: Int = 15
): PaginatedResponse<ReleaseApi> = request {
    get("anime/releases/list") {
        parameter("page", page)
        parameter("limit", limit)
        ids?.let { parameter("ids", it.joinToString(",")) }
        aliases?.let { parameter("aliases", it.joinToString(",")) }
    }
}

suspend fun AnilibriaClient.getRelease(
    aliasOrId: String
): ReleaseApi = request {
    get("anime/releases/${aliasOrId}")
}

suspend fun AnilibriaClient.getReleaseMembers(
    aliasOrId: String
): List<ReleaseMemberApi> = request {
    get("anime/releases/$aliasOrId/members")
}
