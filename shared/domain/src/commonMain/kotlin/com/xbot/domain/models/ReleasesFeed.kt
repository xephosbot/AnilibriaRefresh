package com.xbot.domain.models

data class ReleasesFeed(
    val recommendedReleases: List<Release>,
    val scheduleNow: List<Schedule>,
    val bestNow: List<Release>,
    val bestAllTime: List<Release>,
    val recommendedFranchises: List<Franchise>,
    val genres: List<Genre>
)
