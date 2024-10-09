package com.xbot.data.datasource

import android.util.Log
import com.skydoves.sandwich.onException
import com.skydoves.sandwich.retrofit.serialization.onErrorDeserialize
import com.skydoves.sandwich.suspendOnSuccess
import com.xbot.api.models.misc.TitleUpdate
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
    fun getTitleUpdates(page: Int, limit: Int): Flow<TitlePage> = flow {
        val response = client.getTitleUpdates(
            limit = limit,
            page = page,
            itemsPerPage = NETWORK_PAGE_SIZE,
            filter = listOf("id", "names", "description", "posters", "updated")
        )
        response.suspendOnSuccess(SuccessTitleUpdatedMapper) {
            emit(this)
        }.onErrorDeserialize<TitleUpdate, ErrorMessage> { error ->
            Log.e("TitleDataSource", error.error.message)
            error("HTTP Code: ${error.error.code}, ${error.error.message}")
        }.onException {
            Log.e("TitleDataSource", message ?: "")
            error(message ?: "")
        }
    }.flowOn(Dispatchers.IO)

    companion object {
        const val NETWORK_PAGE_SIZE = 10
    }
}