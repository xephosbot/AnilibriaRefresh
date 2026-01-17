package com.xbot.fixtures.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import arrow.core.Either
import arrow.core.right
import com.xbot.domain.models.DomainError
import com.xbot.domain.models.Release
import com.xbot.domain.models.ReleaseDetail
import com.xbot.domain.models.ReleaseMember
import com.xbot.domain.repository.ReleasesRepository
import com.xbot.fixtures.data.getReleaseDetailMock
import com.xbot.fixtures.data.releaseMocks

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

    override suspend fun getRelease(aliasOrId: String): Either<DomainError, ReleaseDetail> {
        val id = aliasOrId.toIntOrNull()
        return if (id != null) {
            getReleaseDetailMock(id).right()
        } else {
            // Return first or error, let's return first for convenience
            getReleaseDetailMock(releaseMocks.first().id).right()
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

class FakePagingSource(private val data: List<Release>) : PagingSource<Int, Release>() {
    override fun getRefreshKey(state: PagingState<Int, Release>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Release> {
        val page = params.key ?: 0
        val pageSize = params.loadSize
        val start = page * pageSize
        
        if (start >= data.size) {
            return LoadResult.Page(
                data = emptyList(),
                prevKey = if (page > 0) page - 1 else null,
                nextKey = null
            )
        }

        val end = minOf(start + pageSize, data.size)
        val list = data.subList(start, end)
        
        return LoadResult.Page(
            data = list,
            prevKey = if (page > 0) page - 1 else null,
            nextKey = if (end < data.size) page + 1 else null
        )
    }
}
