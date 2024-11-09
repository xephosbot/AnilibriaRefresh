package com.xbot.data.utils

import android.util.Log
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.onException
import com.xbot.data.BuildConfig
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.MissingFieldException
import java.net.UnknownHostException

class NetworkException(message: String?) : Exception(message)
class SerializationException(message: String?, cause: Throwable) : Exception(message, cause)

@OptIn(ExperimentalSerializationApi::class)
fun <T> ApiResponse<T>.handleErrors(tag: String? = null) = onException {
    if (BuildConfig.DEBUG) {
        Log.e(tag ?: DEFAULT_TAG, message ?: UNKNOWN_ERROR)
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

private const val DEFAULT_TAG = "Unknown tag"
private const val UNKNOWN_ERROR = "Unknown error"