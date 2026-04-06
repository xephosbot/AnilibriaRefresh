package com.xbot.domain.models

import io.nlopez.asyncresult.AsyncResult
import io.nlopez.asyncresult.Loading

data class ReleasesFeed(
    val recommendedReleases: AsyncResult<List<Release>> = Loading,
    val scheduleNow: AsyncResult<List<Schedule>> = Loading,
    val bestNow: AsyncResult<List<Release>> = Loading,
    val bestAllTime: AsyncResult<List<Release>> = Loading,
    val recommendedFranchises: AsyncResult<List<Franchise>> = Loading,
    val genres: AsyncResult<List<Genre>> = Loading,
)
