package com.xbot.domain.repository

import com.xbot.domain.models.Episode

interface EpisodesRepository {
    suspend fun getEpisode(episodeId: String): Result<Episode>
    suspend fun getEpisodesByRelease(releaseId: Int): Result<List<Episode>>
}