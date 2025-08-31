package com.xbot.domain.repository

import androidx.paging.PagingSource
import arrow.core.Either
import com.xbot.domain.models.Error
import com.xbot.domain.models.ReleaseMember
import com.xbot.domain.models.Release
import com.xbot.domain.models.ReleaseDetail

interface ReleasesRepository {
    suspend fun getLatestReleases(limit: Int): Either<Error, List<Release>>
    suspend fun getRandomReleases(limit: Int): Either<Error, List<Release>>
    fun getReleasesList(ids: List<Int>? = null, aliases: List<String>? = null): PagingSource<Int, Release>
    suspend fun getRelease(aliasOrId: String): Either<Error, ReleaseDetail>
    suspend fun getReleaseMembers(aliasOrId: String): Either<Error, List<ReleaseMember>>
    suspend fun searchReleases(query: String): Either<Error, List<Release>>
}
