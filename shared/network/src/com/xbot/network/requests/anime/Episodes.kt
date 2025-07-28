package com.xbot.network.requests.anime

import com.xbot.network.client.AnilibriaClient
import com.xbot.network.models.entities.anime.EpisodeWithRelease
import io.ktor.client.request.get

suspend fun AnilibriaClient.getEpisode(
    episodeId: Int
): EpisodeWithRelease = request {
    get("anime/releases/episodes/${episodeId}")
}