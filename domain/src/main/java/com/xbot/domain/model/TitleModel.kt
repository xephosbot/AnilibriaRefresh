package com.xbot.domain.model

data class TitleModel(
    val id: Int,
    val name: String,
    val description: String,
    val tags: List<String>,
    val poster: PosterModel,
    val uploadedTime: String?
)

data class PosterModel(
    val src: String?,
    val thumbnail: String?
)
