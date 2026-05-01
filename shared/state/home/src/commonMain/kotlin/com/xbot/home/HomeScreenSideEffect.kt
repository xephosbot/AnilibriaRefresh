package com.xbot.home

sealed interface HomeScreenSideEffect {
    data class ShowErrorMessage(
        val error: Throwable,
        val onRetry: () -> Unit,
    ) : HomeScreenSideEffect
}
