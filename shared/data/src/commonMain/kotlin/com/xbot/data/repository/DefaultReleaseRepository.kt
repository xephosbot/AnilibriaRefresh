package com.xbot.data.repository

import androidx.paging.PagingSource
import com.xbot.api.client.AnilibriaClient
import com.xbot.api.models.shared.ReleaseApi
import com.xbot.api.request.getCatalogReleases
import com.xbot.api.request.getRandomReleases
import com.xbot.api.request.getRelease
import com.xbot.api.request.getScheduleWeek
import com.xbot.data.datasource.CommonPagingSource
import com.xbot.data.mapper.toDayOfWeek
import com.xbot.data.mapper.toDomain
import com.xbot.data.mapper.toReleaseDetail
import com.xbot.domain.models.ReleaseDetail
import com.xbot.domain.models.Release
import com.xbot.domain.models.utils.PagedResponse
import com.xbot.domain.repository.ReleaseRepository
import kotlinx.datetime.DayOfWeek

internal class DefaultReleaseRepository(
    private val client: AnilibriaClient
) : ReleaseRepository {
    override fun getReleasePagingSource(search: String?): PagingSource<Int, Release> {
        return CommonPagingSource(
            loadPage = { page, limit ->
                val result = client.getCatalogReleases(
                    page = page,
                    limit = limit,
                    search = search
                )
                PagedResponse(
                    items = result.data.map(ReleaseApi::toDomain),
                    total = result.meta.pagination.total
                )
            }
        )
    }

    override suspend fun getRecommendedReleases(): Result<List<Release>> = runCatching {
        // просто заглушка выдающая рандомные релизы
        client.getRandomReleases(10).map(ReleaseApi::toDomain)
    }

    override suspend fun getScheduleWeek(): Result<Map<DayOfWeek, List<Release>>> = runCatching {
        client.getScheduleWeek()
            .map { it.release }
            .groupBy(
                keySelector = { title ->
                    title.publishDay!!.toDayOfWeek()
                },
                valueTransform = ReleaseApi::toDomain,
            )
    }

    override suspend fun getRelease(id: Int): Result<ReleaseDetail> = runCatching {
        client.getRelease(id).toReleaseDetail()
    }
}
