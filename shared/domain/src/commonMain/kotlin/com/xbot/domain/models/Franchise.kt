package com.xbot.domain.models

data class Franchise(
    val id: String,
    val name: String,
    val englishName: String,
    val rating: Double? = null,
    val lastYear: Int? = null,
    val firstYear: Int? = null,
    val totalReleases: Int,
    val totalEpisodes: Int? = null,
    val totalDuration: String? = null,
    val totalDurationInSeconds: Long,
    val poster: Poster?,
    val franchiseReleases: List<Release>? = null,
)
