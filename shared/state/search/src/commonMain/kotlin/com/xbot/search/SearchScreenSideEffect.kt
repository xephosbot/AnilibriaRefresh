package com.xbot.search

sealed interface SearchScreenSideEffect {
    data class ShowErrorMessage(
        val error: Throwable,
        val onRetry: () -> Unit,
    ) : SearchScreenSideEffect
}
