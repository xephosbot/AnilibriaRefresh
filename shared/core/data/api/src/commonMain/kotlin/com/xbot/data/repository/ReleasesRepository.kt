package com.xbot.data.repository

import androidx.paging.PagingSource
import arrow.core.Either
import com.xbot.common.error.AppError
import com.xbot.domain.models.ReleaseMember
import com.xbot.domain.models.Release
import com.xbot.domain.models.ReleaseDetails

interface ReleasesRepository {
    suspend fun getLatestReleases(limit: Int): Either<AppError, List<Release>>
    suspend fun getRandomReleases(limit: Int): Either<AppError, List<Release>>
    fun getReleasesList(ids: List<Int>? = null, aliases: List<String>? = null): PagingSource<Int, Release>
    suspend fun getRelease(aliasOrId: String): Either<AppError, ReleaseDetails>
    suspend fun getReleaseMembers(aliasOrId: String): Either<AppError, List<ReleaseMember>>
    suspend fun searchReleases(query: String): Either<AppError, List<Release>>
}
