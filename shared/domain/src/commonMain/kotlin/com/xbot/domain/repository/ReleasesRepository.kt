package com.xbot.domain.repository

import androidx.paging.PagingSource
import arrow.core.Either
import com.xbot.domain.models.DomainError
import com.xbot.domain.models.ReleaseMember
import com.xbot.domain.models.Release
import com.xbot.domain.models.ReleaseDetails

interface ReleasesRepository {
    suspend fun getLatestReleases(limit: Int): Either<DomainError, List<Release>>
    suspend fun getRandomReleases(limit: Int): Either<DomainError, List<Release>>
    fun getReleasesList(ids: List<Int>? = null, aliases: List<String>? = null): PagingSource<Int, Release>
    suspend fun getRelease(aliasOrId: String): Either<DomainError, ReleaseDetails>
    suspend fun getReleaseMembers(aliasOrId: String): Either<DomainError, List<ReleaseMember>>
    suspend fun searchReleases(query: String): Either<DomainError, List<Release>>
}
