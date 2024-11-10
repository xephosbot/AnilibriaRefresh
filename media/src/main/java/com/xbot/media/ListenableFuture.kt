package com.xbot.media

import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import com.google.common.util.concurrent.Uninterruptibles
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.concurrent.ExecutionException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

internal suspend fun <T> ListenableFuture<T>.await(): T {
    try {
        if (isDone) return Uninterruptibles.getUninterruptibly(this)
    } catch (e: ExecutionException) {
        throw e.nonNullCause()
    }

    return suspendCancellableCoroutine { cont: CancellableContinuation<T> ->
        addListener(
            ToContinuation(this, cont),
            MoreExecutors.directExecutor(),
        )
        cont.invokeOnCancellation {
            cancel(false)
        }
    }
}

private class ToContinuation<T>(
    val futureToObserve: ListenableFuture<T>,
    val continuation: CancellableContinuation<T>,
) : Runnable {
    override fun run() {
        if (futureToObserve.isCancelled) {
            continuation.cancel()
        } else {
            try {
                continuation.resume(Uninterruptibles.getUninterruptibly(futureToObserve))
            } catch (e: ExecutionException) {
                continuation.resumeWithException(e.nonNullCause())
            }
        }
    }
}

private fun ExecutionException.nonNullCause(): Throwable {
    return this.cause!!
}
