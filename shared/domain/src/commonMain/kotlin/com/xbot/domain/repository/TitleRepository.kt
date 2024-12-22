package com.xbot.domain.repository

import androidx.paging.PagingSource
import com.xbot.domain.models.TitleDetailModel
import com.xbot.domain.models.TitleModel
import com.xbot.domain.models.enums.DayOfWeek

interface TitleRepository {
    fun getTitlePagingSource(): PagingSource<Int, TitleModel>
    suspend fun getRecommendedTitles(): Result<List<TitleModel>>
    suspend fun getScheduleWeek(): Result<Map<DayOfWeek, List<TitleModel>>>
    suspend fun getTitle(id: Int): Result<TitleDetailModel>
}
