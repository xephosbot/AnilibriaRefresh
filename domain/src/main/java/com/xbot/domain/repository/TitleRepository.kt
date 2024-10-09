package com.xbot.domain.repository

import androidx.paging.PagingData
import com.xbot.domain.model.TitleModel
import kotlinx.coroutines.flow.Flow

interface TitleRepository {
    fun getLatestTitles(): Flow<PagingData<TitleModel>>
}