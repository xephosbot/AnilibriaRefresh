package com.xbot.data.repository

import com.xbot.api.client.AnilibriaClient
import com.xbot.api.models.shared.Release
import com.xbot.api.request.getCatalogReleases
import com.xbot.api.request.getRandomReleases
import com.xbot.api.request.getRelease
import com.xbot.api.request.getScheduleWeek
import com.xbot.data.mapper.toDayOfWeek
import com.xbot.data.mapper.toTitleDetailModel
import com.xbot.data.mapper.toTitleModel
import com.xbot.domain.models.TitleDetailModel
import com.xbot.domain.models.TitleModel
import com.xbot.domain.models.enums.DayOfWeek
import com.xbot.domain.models.utils.PagedResponse
import com.xbot.domain.repository.TitleRepository

class TitleRepositoryImpl(
    private val client: AnilibriaClient
) : TitleRepository {
    override suspend fun getCatalogTitles(page: Int, limit: Int): PagedResponse<TitleModel> {
        val titles = client.getCatalogReleases(page, limit)
        return PagedResponse(
            items = titles.data.map(Release::toTitleModel),
            total = titles.meta.pagination.total
        )
    }

    override suspend fun getRecommendedTitles(): List<TitleModel> {
        // TODO: просто заглушка выдающая рандомные релизы
        return client.getRandomReleases(10).map(Release::toTitleModel)
    }

    override suspend fun getScheduleWeek(): Map<DayOfWeek, List<TitleModel>> {
        return client.getScheduleWeek()
            .map { it.release }
            .groupBy(
                keySelector = { title ->
                    title.publishDay.value.toDayOfWeek()
                },
                valueTransform = Release::toTitleModel,
            )
    }

    override suspend fun getTitle(id: Int): TitleDetailModel {
        return client.getRelease(id).toTitleDetailModel()
    }
}
