package com.xbot.data.fixtures.repository

import androidx.paging.PagingSource
import arrow.core.Either
import arrow.core.right
import com.xbot.data.fixtures.datasource.FakePagingSource
import com.xbot.data.repository.ReleasesRepository
import com.xbot.domain.fixtures.data.getReleaseDetailMock
import com.xbot.domain.fixtures.data.releaseMocks
import com.xbot.domain.models.DomainError
import com.xbot.domain.models.Release
import com.xbot.domain.models.ReleaseDetails
import com.xbot.domain.models.ReleaseMember

class FakeReleasesRepository : ReleasesRepository {
    override suspend fun getLatestReleases(limit: Int): Either<DomainError, List<Release>> {
        return releaseMocks.take(limit).right()
    }

    override suspend fun getRandomReleases(limit: Int): Either<DomainError, List<Release>> {
        return releaseMocks.shuffled().take(limit).right()
    }

    override fun getReleasesList(ids: List<Int>?, aliases: List<String>?): PagingSource<Int, Release> {
        return FakePagingSource(releaseMocks)
    }

    override suspend fun getRelease(aliasOrId: String): Either<DomainError, ReleaseDetails> {
        val id = aliasOrId.toIntOrNull()
        return if (id != null) {
            getReleaseDetailMock(id).details.right()
        } else {
            // Return first or error, let's return first for convenience
            getReleaseDetailMock(releaseMocks.first().id).details.right()
        }
    }

    override suspend fun getReleaseMembers(aliasOrId: String): Either<DomainError, List<ReleaseMember>> {
        return emptyList<ReleaseMember>().right()
    }

    override suspend fun searchReleases(query: String): Either<DomainError, List<Release>> {
        return releaseMocks.filter { 
            it.name.contains(query, ignoreCase = true) || 
            (it.englishName?.contains(query, ignoreCase = true) == true)
        }.right()
    }
}
