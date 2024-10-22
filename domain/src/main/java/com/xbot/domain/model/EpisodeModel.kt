package com.xbot.domain.model

data class EpisodeModel(
    val id: String,
    val name: String?,
    val ordinal: Int,
    val preview: PosterModel,
    val duration: Int
)
