package com.xbot.data.repository

import com.skydoves.sandwich.onException
import com.skydoves.sandwich.retrofit.serialization.onErrorDeserialize
import com.skydoves.sandwich.suspendOnSuccess
import com.xbot.api.models.misc.TitleUpdate
import com.xbot.api.models.title.Title
import com.xbot.api.service.AnilibriaClient
import com.xbot.data.models.ErrorMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class TestRepository @Inject constructor(
    private val client: AnilibriaClient
) {
    fun getTitle(id: Int, onError: (String?) -> Unit): Flow<Title> = flow {
        val response = client.getTitle(id, filter = listOf("id", "names", "posters", "type", "status"))
        response.suspendOnSuccess {
            emit(data)
        }.onErrorDeserialize<Title, ErrorMessage> { error ->
            onError(error.error.message)
        }.onException {
            onError(message)
        }
    }.flowOn(Dispatchers.IO)

    fun getTitleUpdates(onError: (String?) -> Unit): Flow<List<Title>> = flow {
        val response = client.getTitleUpdates(limit = 20, filter = listOf("id", "names", "posters", "type", "status"))
        response.suspendOnSuccess {
            emit(data.list)
        }.onErrorDeserialize<TitleUpdate, ErrorMessage> { error ->
            onError(error.error.message)
        }.onException {
            onError(message)
        }
    }.flowOn(Dispatchers.IO)
}
