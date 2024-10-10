package com.xbot.data.datasource

import android.util.Log
import com.skydoves.sandwich.onException
import com.skydoves.sandwich.retrofit.serialization.onErrorDeserialize
import com.skydoves.sandwich.suspendOnSuccess
import com.xbot.api.models.AnimeCatalogResponse
import com.xbot.api.service.AnilibriaClient
import com.xbot.data.mapper.SuccessTitleUpdatedMapper
import com.xbot.data.models.ErrorMessage
import com.xbot.data.models.TitlePage
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
        }.onErrorDeserialize<AnimeCatalogResponse, ErrorMessage> { error ->
            Log.e("TitleDataSource", error.description)
            error("HTTP Code: ${error.code}, ${error.description}")
        }.onException {
            Log.e("TitleDataSource", message ?: "")
            error(message ?: "")
        }
    }.flowOn(Dispatchers.IO)

    companion object {
        const val NETWORK_PAGE_SIZE = 10
    }
}