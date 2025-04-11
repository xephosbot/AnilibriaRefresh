package com.xbot.shared.domain.models

import kotlinx.datetime.LocalDateTime
import kotlin.time.Duration

data class Episode(
    val id: String,
    val name: String?,
    val englishName: String?,
    val ordinal: Float,
    val preview: Poster?,
    val hls480: String?,
    val hls720: String?,
    val hls1080: String?,
    val duration: Duration,
    val updatedAt: LocalDateTime,
)
