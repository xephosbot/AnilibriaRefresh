package com.xbot.data.datasource

import SuccessTitleMapper
import android.util.Log
import com.skydoves.sandwich.onException
import com.skydoves.sandwich.retrofit.serialization.onErrorDeserialize
import com.skydoves.sandwich.suspendOnSuccess
import com.xbot.api.models.AnimeCatalogResponse
import com.xbot.api.service.AnilibriaClient
import com.xbot.data.mapper.SuccessTitleUpdatedMapper
import com.xbot.data.models.ErrorMessage
import com.xbot.data.models.TitlePage
import com.xbot.domain.model.TitleModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class TitleDataSource @Inject constructor(
    private val client: AnilibriaClient
) {
    //TODO: Более подробная обработка ошибок
    fun getTitleUpdates(page: Int, limit: Int): Flow<TitlePage> = flow {
        val response = client.getReleases(
            page = page,
            limit = limit
        )
        response.suspendOnSuccess(SuccessTitleUpdatedMapper) {
            emit(this)
        }.onException {
            Log.e("TitleDataSource", message ?: "")
            error(message ?: "")
        }
    }.flowOn(Dispatchers.IO)

    fun getTitle(id: Int): Flow<TitleModel> = flow {
        val response = client.getRelease(id)
        response.suspendOnSuccess(SuccessTitleMapper) {
            emit(this)
        }.onException {
            Log.e("TitleDataSource", message ?: "")
            error(message ?: "")
        }
    }

    companion object {
        const val NETWORK_PAGE_SIZE = 10
    }
}