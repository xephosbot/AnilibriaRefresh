package com.xbot.domain.repository

import androidx.paging.PagingSource
import com.xbot.domain.models.ReleaseMember
import com.xbot.domain.models.Release
import com.xbot.domain.models.ReleaseDetail
import kotlinx.datetime.DayOfWeek

interface ReleasesRepository {
    suspend fun getLatestReleases(limit: Int): Result<List<Release>>
    suspend fun getRandomReleases(limit: Int): Result<List<Release>>
    fun getReleasesList(ids: List<Int>? = null, aliases: List<String>? = null): PagingSource<Int, Release>
    suspend fun getRelease(aliasOrId: String): Result<ReleaseDetail>
    suspend fun getReleaseMembers(aliasOrId: String): Result<List<ReleaseMember>>
    suspend fun searchReleases(query: String): Result<List<Release>>
}
