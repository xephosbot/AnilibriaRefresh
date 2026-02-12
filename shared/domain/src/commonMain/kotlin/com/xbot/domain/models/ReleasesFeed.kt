package com.xbot.domain.models

data class ReleasesFeed(
    val recommendedReleases: List<Release?> = List(10) { null },
    val scheduleNow: List<Schedule?> = List(10) { null },
    val bestNow: List<Release?> = List(10) { null },
    val bestAllTime: List<Release?> = List(10) { null },
    val recommendedFranchises: List<Franchise?> = List(10) { null },
    val genres: List<Genre?> = List(10) { null },
)
