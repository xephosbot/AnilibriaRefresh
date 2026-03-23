package com.xbot.common.lifecycle

import androidx.annotation.CheckResult
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner

@CheckResult
private fun LifecycleOwner.dropUnlessStateIsAtLeast(
    state: Lifecycle.State,
    block: () -> Unit
): () -> Unit {
    require(state != Lifecycle.State.DESTROYED) {
        "Target state is not allowed to be Lifecycle.State.DESTROYED " +
                "because callbacks should not be invoked after destruction."
    }

    return {
        if (lifecycle.currentState.isAtLeast(state)) {
            block()
        }
    }
}

@CheckResult
fun LifecycleOwner.dropUnlessStarted(
    block: () -> Unit
): () -> Unit = dropUnlessStateIsAtLeast(Lifecycle.State.STARTED, block)

@CheckResult
fun LifecycleOwner.dropUnlessResumed(
    block: () -> Unit
): () -> Unit = dropUnlessStateIsAtLeast(Lifecycle.State.RESUMED, block)
