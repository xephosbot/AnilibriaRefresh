package com.xbot.domain.models

data class ReleasesFeed(
    val recommendedReleases: List<Release?>,
    val scheduleNow: List<Schedule?>,
    val bestNow: List<Release?>,
    val bestAllTime: List<Release?>,
    val recommendedFranchises: List<Franchise?>,
    val genres: List<Genre?>,
) {
    companion object {
        const val PLACEHOLDER_COUNT = 10

        private fun <T> emptyPlaceholderList(): List<T?> = List(PLACEHOLDER_COUNT) { null }

        fun create(
            recommendedReleases: List<Release?>? = null,
            scheduleNow: List<Schedule?>? = null,
            bestNow: List<Release?>? = null,
            bestAllTime: List<Release?>? = null,
            recommendedFranchises: List<Franchise?>? = null,
            genres: List<Genre?>? = null,
        ) = ReleasesFeed(
            recommendedReleases = recommendedReleases ?: emptyPlaceholderList(),
            scheduleNow = scheduleNow ?: emptyPlaceholderList(),
            bestNow = bestNow ?: emptyPlaceholderList(),
            bestAllTime = bestAllTime ?: emptyPlaceholderList(),
            recommendedFranchises = recommendedFranchises ?: emptyPlaceholderList(),
            genres = genres ?: emptyPlaceholderList(),
        )
    }
}