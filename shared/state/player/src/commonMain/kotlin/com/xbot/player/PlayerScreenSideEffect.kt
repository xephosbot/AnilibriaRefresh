package com.xbot.player

sealed interface PlayerScreenSideEffect {
    data class ShowLoadError(
        val error: Throwable,
        val onRetry: () -> Unit,
    ) : PlayerScreenSideEffect
}
