package com.xbot.domain.models

data class Schedule(
    val release: Release,
    val fullSeasonIsReleased: Boolean,
    val publishedReleaseEpisode: Episode
)