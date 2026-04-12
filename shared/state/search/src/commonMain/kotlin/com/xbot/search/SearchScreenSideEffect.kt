package com.xbot.search

sealed interface SearchScreenSideEffect {
    data class ShowLoadError(
        val error: Throwable,
        val onRetry: () -> Unit,
    ) : SearchScreenSideEffect
}
