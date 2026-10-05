package com.xbot.network.utils

import io.ktor.client.engine.darwin.DarwinHttpRequestException
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.utils.unwrapCancellationException
import io.ktor.util.network.UnresolvedAddressException
import platform.Foundation.NSURLErrorCannotConnectToHost
import platform.Foundation.NSURLErrorDomain
import platform.Foundation.NSURLErrorNotConnectedToInternet
import platform.Foundation.NSURLErrorTimedOut

internal actual fun Throwable.isNoConnectionException(): Boolean {
    val exception = unwrapCancellationException()
    val nsError = (exception as? DarwinHttpRequestException)?.origin
    if (nsError != null) {
        if (nsError.domain != NSURLErrorDomain) return false
        return when (nsError.code) {
            NSURLErrorNotConnectedToInternet,
            NSURLErrorTimedOut,
            NSURLErrorCannotConnectToHost
            -> true

            else -> false
        }
    }
    return when (exception) {
        is UnresolvedAddressException -> true
        is SocketTimeoutException -> true
        else -> false
    }
}
