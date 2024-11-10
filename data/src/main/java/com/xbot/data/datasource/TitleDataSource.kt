package com.xbot.data.datasource

import com.skydoves.sandwich.suspendOnSuccess
import com.xbot.api.service.AnilibriaClient
import com.xbot.data.mapper.SuccessScheduleMapper
import com.xbot.data.mapper.SuccessTitleMapper
import com.xbot.data.mapper.SuccessTitlesMapper
import com.xbot.data.mapper.SuccessTitlesUpdatedMapper
import com.xbot.data.models.TitlePage
import com.xbot.data.utils.handleErrors
import com.xbot.domain.models.TitleDetailModel
import com.xbot.domain.models.TitleModel
import com.xbot.domain.models.enums.DayOfWeek
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class TitleDataSource(
    private val client: AnilibriaClient,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) {
    fun getLatestTitles(page: Int, limit: Int): Flow<TitlePage> = flow {
        val response = client.getReleases(page, limit)
        response.suspendOnSuccess(SuccessTitlesUpdatedMapper) {
            emit(this)
        }.handleErrors(TAG)
    }.flowOn(dispatcher)

    fun getRecommendedTitles(limit: Int): Flow<List<TitleModel>> = flow {
        // TODO: просто заглушка выдающая рандомные релизы
        val response = client.getRandomReleases(limit)
        response.suspendOnSuccess(SuccessTitlesMapper) {
            emit(this)
        }.handleErrors(TAG)
    }.flowOn(dispatcher)

    fun getScheduleTitles(): Flow<Map<DayOfWeek, List<TitleModel>>> = flow {
        val response = client.getScheduleWeek()
        response.suspendOnSuccess(SuccessScheduleMapper) {
            emit(this)
        }.handleErrors(TAG)
    }.flowOn(dispatcher)

    fun getTitle(id: Int): Flow<TitleDetailModel> = flow {
        val response = client.getRelease(id)
        response.suspendOnSuccess(SuccessTitleMapper) {
            emit(this)
        }.handleErrors(TAG)
    }.flowOn(dispatcher)

    companion object {
        const val NETWORK_PAGE_SIZE = 10
        private const val TAG = "TitleDataSource"
    }
}
