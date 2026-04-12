package com.xbot.home

sealed interface HomeScreenSideEffect {
    data class ShowLoadError(
        val error: Throwable,
        val onRetry: () -> Unit,
    ) : HomeScreenSideEffect
}
