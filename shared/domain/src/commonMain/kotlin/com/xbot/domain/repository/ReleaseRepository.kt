package com.xbot.domain.repository

import androidx.paging.PagingSource
import com.xbot.domain.models.ReleaseDetail
import com.xbot.domain.models.Release
import kotlinx.datetime.DayOfWeek

interface ReleaseRepository {
    fun getReleasePagingSource(): PagingSource<Int, Release>
    suspend fun getRecommendedReleases(): Result<List<Release>>
    suspend fun getScheduleWeek(): Result<Map<DayOfWeek, List<Release>>>
    suspend fun getRelease(id: Int): Result<ReleaseDetail>
}
