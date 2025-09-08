package com.xbot.domain.models

data class ReleasesFeed(
    val recommendedReleases: List<Release>,
    val scheduleNow: List<Schedule>,
    val bestReleases: List<Release>,
    val recommendedFranchises: List<Franchise>,
    val genres: List<Genre>
)
