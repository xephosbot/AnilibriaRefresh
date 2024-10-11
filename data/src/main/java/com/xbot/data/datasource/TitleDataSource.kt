package com.xbot.data.datasource

import SuccessTitleMapper
import android.util.Log
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.onException
import com.skydoves.sandwich.suspendOnSuccess
import com.xbot.api.service.AnilibriaClient
import com.xbot.data.BuildConfig
import com.xbot.data.mapper.SuccessTitleUpdatedMapper
import com.xbot.data.models.NetworkException
import com.xbot.data.models.SerializationException
import com.xbot.data.models.TitlePage
import com.xbot.domain.model.TitleModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.MissingFieldException
import java.net.UnknownHostException
import javax.inject.Inject

class TitleDataSource @Inject constructor(
    private val client: AnilibriaClient
) {
    fun getTitleUpdates(page: Int, limit: Int): Flow<TitlePage> = flow {
        val response = client.getReleases(page, limit)
        response.suspendOnSuccess(SuccessTitleUpdatedMapper) {
            emit(this)
        }.handleErrors()
    }.flowOn(Dispatchers.IO)

    fun getTitle(id: Int): Flow<TitleModel> = flow {
        val response = client.getRelease(id)
        response.suspendOnSuccess(SuccessTitleMapper) {
            emit(this)
        }.handleErrors()
    }

    @OptIn(ExperimentalSerializationApi::class)
    private fun <T> ApiResponse<T>.handleErrors() = onException {
        if (BuildConfig.DEBUG) {
            Log.e(TAG, message ?: UNKNOWN_ERROR)
        }
        when (throwable) {
            is UnknownHostException -> {
                throw NetworkException(throwable.message)
            }
            is MissingFieldException -> {
                throw SerializationException(throwable.message, throwable)
            }
            else -> throw throwable
        }
    }

    companion object {
        const val NETWORK_PAGE_SIZE = 10
        private const val TAG = "TitleDataSource"
        private const val UNKNOWN_ERROR = "Unknown error"
    }
}