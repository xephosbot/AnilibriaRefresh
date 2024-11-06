package com.xbot.domain.model

data class TitleModel(
    val id: Int,
    val year: Int,
    val type: String,
    val name: String,
    val description: String,
    val episodesCount: Int?,
    val favoritesCount: Int,
    val poster: PosterModel
)