package com.xbot.domain.models

import kotlinx.datetime.LocalDateTime

data class Episode(
    val id: String,
    val name: String?,
    val englishName: String?,
    val ordinal: Float,
    val preview: Poster,
    val hls480: String?,
    val hls720: String?,
    val hls1080: String?,
    val duration: Int,
    val updatedAt: LocalDateTime,
)
