package com.xbot.domain.repository

import androidx.paging.PagingData
import com.xbot.domain.models.TitleDetailModel
import com.xbot.domain.models.TitleModel
import com.xbot.domain.models.enums.DayOfWeek
import kotlinx.coroutines.flow.Flow

interface TitleRepository {
    fun getLatestTitles(): Flow<PagingData<TitleModel>>
    suspend fun getRecommendedTitles(): List<TitleModel>
    suspend fun getScheduleTitles(): Map<DayOfWeek, List<TitleModel>>
    suspend fun getTitle(id: Int): TitleDetailModel
}
