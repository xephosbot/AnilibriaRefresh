package com.xbot.domain.models

data class Franchise(
    val id: String,
    val name: String,
    val englishName: String,
    val rating: Double? = null,
    val lastYear: Int,
    val firstYear: Int,
    val totalReleases: Int,
    val totalEpisodes: Int,
    val totalDuration: String,
    val totalDurationInSeconds: Long,
    val poster: Poster?,
    val franchiseReleases: List<Release>? = null,
)
