package com.xbot.domain.models

import com.xbot.domain.models.enums.ReleaseType

data class Release(
    val id: Int,
    val type: ReleaseType?,
    val year: Int,
    val name: String,
    val description: String,
    val episodesCount: Int?,
    val favoritesCount: Int,
    val poster: Poster,
)
