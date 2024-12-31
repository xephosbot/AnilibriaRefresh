package com.xbot.domain.models

data class Episode(
    val id: String,
    val name: String?,
    val ordinal: Float,
    val preview: Poster,
    val hls480: String?,
    val hls720: String?,
    val hls1080: String?,
    val duration: Int,
)
