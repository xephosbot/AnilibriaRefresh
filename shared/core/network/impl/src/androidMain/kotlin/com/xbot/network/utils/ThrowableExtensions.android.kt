package com.xbot.network.utils

import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.utils.unwrapCancellationException
import io.ktor.util.network.UnresolvedAddressException
import java.net.ConnectException
import java.net.UnknownHostException

internal actual fun Throwable.isNoConnectionException(): Boolean {
    val exception = unwrapCancellationException()
    return when (exception) {
        is UnresolvedAddressException -> true
        is UnknownHostException, is SocketTimeoutException, is ConnectException -> true
        else -> false
    }
}
