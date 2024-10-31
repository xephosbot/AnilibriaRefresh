package com.xbot.domain.model

data class EpisodeModel(
    val id: String,
    val name: String?,
    val ordinal: Int,
    val preview: PosterModel,
    val hls480: String?,
    val hls720: String?,
    val hls1080: String?,
    val duration: Int
)
