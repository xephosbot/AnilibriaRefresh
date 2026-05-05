package com.xbot.domain.models

import kotlinx.datetime.LocalDateTime
import kotlin.time.Duration

data class Episode(
    val id: String,
    val name: String? = null,
    val englishName: String? = null,
    val ordinal: Float,
    val preview: Poster? = null,
    val hls480: String? = null,
    val hls720: String? = null,
    val hls1080: String? = null,
    val duration: Duration? = null,
    val updatedAt: LocalDateTime? = null,
)

val Episode.hlsUrl: String?
    get() = hls1080 ?: hls720 ?: hls480
