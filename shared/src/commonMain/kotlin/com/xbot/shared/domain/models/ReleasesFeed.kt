package com.xbot.shared.domain.models

import kotlinx.datetime.DayOfWeek

data class ReleasesFeed(
    val recommendedReleases: List<Release>,
    val schedule: Map<DayOfWeek, List<Release>>,
    val genres: List<Genre>
)
