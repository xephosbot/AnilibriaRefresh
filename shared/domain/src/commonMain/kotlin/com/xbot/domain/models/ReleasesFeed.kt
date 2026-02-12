package com.xbot.domain.models

data class ReleasesFeed(
    val recommendedReleases: List<Release?> = List(PLACEHOLDER_COUNT) { null },
    val scheduleNow: List<Schedule?> = List(PLACEHOLDER_COUNT) { null },
    val bestNow: List<Release?> = List(PLACEHOLDER_COUNT) { null },
    val bestAllTime: List<Release?> = List(PLACEHOLDER_COUNT) { null },
    val recommendedFranchises: List<Franchise?> = List(PLACEHOLDER_COUNT) { null },
    val genres: List<Genre?> = List(PLACEHOLDER_COUNT) { null },
) {
    companion object {
        private const val PLACEHOLDER_COUNT = 10
    }
}
