package com.xbot.domain.models

import kotlin.time.Duration
import kotlinx.datetime.LocalDateTime

data class EpisodeProgress(
    val episodeId: String,
    val position: Duration,
    val isWatched: Boolean,
    val updatedAt: LocalDateTime
)
