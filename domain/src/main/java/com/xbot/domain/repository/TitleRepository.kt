package com.xbot.domain.repository

import androidx.paging.PagingData
import com.xbot.domain.model.DayOfWeek
import com.xbot.domain.model.TitleDetailModel
import com.xbot.domain.model.TitleModel
import kotlinx.coroutines.flow.Flow

interface TitleRepository {
    fun getLatestTitles(): Flow<PagingData<TitleModel>>
    fun getRecommendedTitles(): Flow<List<TitleModel>>
    fun getScheduleTitles(): Flow<Map<DayOfWeek, List<TitleModel>>>
    fun getTitle(id: Int): Flow<TitleDetailModel>
}