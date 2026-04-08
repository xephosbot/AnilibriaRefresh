package com.xbot.player

sealed interface PlayerScreenSideEffect {
    data class ShowErrorMessage(
        val error: Throwable,
        val onRetry: () -> Unit,
    ) : PlayerScreenSideEffect
}
