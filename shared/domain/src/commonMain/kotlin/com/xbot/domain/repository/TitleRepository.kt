package com.xbot.domain.repository

import com.xbot.domain.models.TitleDetailModel
import com.xbot.domain.models.TitleModel
import com.xbot.domain.models.enums.DayOfWeek
import com.xbot.domain.models.utils.PagedResponse

interface TitleRepository {
    suspend fun getCatalogTitles(page: Int, limit: Int): PagedResponse<TitleModel>
    suspend fun getRecommendedTitles(): List<TitleModel>
    suspend fun getScheduleWeek(): Map<DayOfWeek, List<TitleModel>>
    suspend fun getTitle(id: Int): TitleDetailModel
}
