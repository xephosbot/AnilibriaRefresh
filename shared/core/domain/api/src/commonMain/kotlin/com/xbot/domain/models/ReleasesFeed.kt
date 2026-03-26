package com.xbot.domain.models

import io.nlopez.asyncresult.AsyncResult
import io.nlopez.asyncresult.Loading

data class ReleasesFeed(
    val recommendedReleases: AsyncResult<List<Release>>,
    val scheduleNow: AsyncResult<List<Schedule>>,
    val bestNow: AsyncResult<List<Release>>,
    val bestAllTime: AsyncResult<List<Release>>,
    val recommendedFranchises: AsyncResult<List<Franchise>>,
    val genres: AsyncResult<List<Genre>>,
) {
    companion object {
        fun create(
            recommendedReleases: AsyncResult<List<Release>> = Loading,
            scheduleNow: AsyncResult<List<Schedule>> = Loading,
            bestNow: AsyncResult<List<Release>> = Loading,
            bestAllTime: AsyncResult<List<Release>> = Loading,
            recommendedFranchises: AsyncResult<List<Franchise>> = Loading,
            genres: AsyncResult<List<Genre>> = Loading,
        ) = ReleasesFeed(
            recommendedReleases = recommendedReleases,
            scheduleNow = scheduleNow,
            bestNow = bestNow,
            bestAllTime = bestAllTime,
            recommendedFranchises = recommendedFranchises,
            genres = genres,
        )
    }
}