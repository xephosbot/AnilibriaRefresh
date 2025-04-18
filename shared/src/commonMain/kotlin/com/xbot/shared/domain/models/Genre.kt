package com.xbot.shared.domain.models

data class Genre(
    val id: Int,
    val name: String,
    val releasesCount: Int?,
    val image: Poster?,
)
