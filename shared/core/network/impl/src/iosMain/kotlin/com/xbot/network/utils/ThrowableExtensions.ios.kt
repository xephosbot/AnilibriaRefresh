package com.xbot.network.utils

import io.ktor.client.engine.darwin.DarwinHttpRequestException
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.utils.unwrapCancellationException
import io.ktor.util.network.UnresolvedAddressException
import platform.Foundation.NSURLErrorDomain
import platform.Foundation.NSURLErrorNotConnectedToInternet
import platform.Foundation.NSURLErrorTimedOut

internal actual fun Throwable.isNoConnectionException(): Boolean {
    val exception = unwrapCancellationException()
    val nsError = (exception as? DarwinHttpRequestException)?.origin
    if (nsError != null) {
        return nsError.domain == NSURLErrorDomain && (nsError.code == NSURLErrorNotConnectedToInternet || nsError.code == NSURLErrorTimedOut || nsError.code == -1004L)
    }
    return when (exception) {
        is UnresolvedAddressException -> true
        is SocketTimeoutException -> true
        else -> false
    }
}
