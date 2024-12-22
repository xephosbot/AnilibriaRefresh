package com.xbot.data.repository

import androidx.paging.PagingSource
import com.xbot.api.client.AnilibriaClient
import com.xbot.api.models.shared.Release
import com.xbot.api.request.getCatalogReleases
import com.xbot.api.request.getRandomReleases
import com.xbot.api.request.getRelease
import com.xbot.api.request.getScheduleWeek
import com.xbot.data.datasource.CommonPagingSource
import com.xbot.data.mapper.toDayOfWeek
import com.xbot.data.mapper.toTitleDetailModel
import com.xbot.data.mapper.toTitleModel
import com.xbot.domain.models.TitleDetailModel
import com.xbot.domain.models.TitleModel
import com.xbot.domain.models.enums.DayOfWeek
import com.xbot.domain.models.utils.PagedResponse
import com.xbot.domain.repository.TitleRepository

internal class DefaultTitleRepository(
    private val client: AnilibriaClient
) : TitleRepository {
    override fun getTitlePagingSource(): PagingSource<Int, TitleModel> {
        return CommonPagingSource(
            loadPage = { page, limit ->
                val result = client.getCatalogReleases(page, limit)
                PagedResponse(
                    items = result.data.map(Release::toTitleModel),
                    total = result.meta.pagination.total
                )
            }
        )
    }

    override suspend fun getRecommendedTitles(): Result<List<TitleModel>> = runCatching {
        // TODO: просто заглушка выдающая рандомные релизы
        client.getRandomReleases(10).map(Release::toTitleModel)
    }

    override suspend fun getScheduleWeek(): Result<Map<DayOfWeek, List<TitleModel>>> = runCatching {
        client.getScheduleWeek()
            .map { it.release }
            .groupBy(
                keySelector = { title ->
                    title.publishDay.toDayOfWeek()
                },
                valueTransform = Release::toTitleModel,
            )
    }

    override suspend fun getTitle(id: Int): Result<TitleDetailModel> = runCatching {
        client.getRelease(id).toTitleDetailModel()
    }
}
