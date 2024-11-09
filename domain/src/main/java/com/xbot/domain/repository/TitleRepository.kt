package com.xbot.domain.repository

import androidx.paging.PagingData
import com.xbot.domain.models.TitleDetailModel
import com.xbot.domain.models.TitleModel
import com.xbot.domain.models.enums.DayOfWeek
import kotlinx.coroutines.flow.Flow

interface TitleRepository {
    fun getLatestTitles(): Flow<PagingData<TitleModel>>
    fun getRecommendedTitles(): Flow<List<TitleModel>>
    fun getScheduleTitles(): Flow<Map<DayOfWeek, List<TitleModel>>>
    fun getTitle(id: Int): Flow<TitleDetailModel>
}