package com.xbot.data.fixtures

import androidx.paging.PagingSource
import arrow.core.Either
import arrow.core.right
import com.xbot.data.repository.ReleasesRepository
import com.xbot.domain.fixtures.ReleaseFixtures
import com.xbot.domain.fixtures.createReleaseDetails
import com.xbot.domain.models.DomainError
import com.xbot.domain.models.Release
import com.xbot.domain.models.ReleaseDetails
import com.xbot.domain.models.ReleaseMember

class FakeReleasesRepository : ReleasesRepository {
    override suspend fun getLatestReleases(limit: Int): Either<DomainError, List<Release>> {
        return ReleaseFixtures.list(limit).right()
    }

    override suspend fun getRandomReleases(limit: Int): Either<DomainError, List<Release>> {
        return ReleaseFixtures.all.shuffled().take(limit).right()
    }

    override fun getReleasesList(ids: List<Int>?, aliases: List<String>?): PagingSource<Int, Release> {
        return FakePagingSource(ReleaseFixtures.all)
    }

    override suspend fun getRelease(aliasOrId: String): Either<DomainError, ReleaseDetails> {
        val id = aliasOrId.toIntOrNull()
        val release = if (id != null) {
            ReleaseFixtures.all.find { it.id == id } ?: ReleaseFixtures.frieren
        } else {
            ReleaseFixtures.frieren
        }
        return createReleaseDetails(release = release).right()
    }

    override suspend fun getReleaseMembers(aliasOrId: String): Either<DomainError, List<ReleaseMember>> {
        return emptyList<ReleaseMember>().right()
    }

    override suspend fun searchReleases(query: String): Either<DomainError, List<Release>> {
        return ReleaseFixtures.all.filter {
            it.name.contains(query, ignoreCase = true) || 
            (it.englishName?.contains(query, ignoreCase = true) == true)
        }.right()
    }
}
