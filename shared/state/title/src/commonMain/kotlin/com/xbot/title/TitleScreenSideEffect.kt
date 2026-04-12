package com.xbot.title

sealed interface TitleScreenSideEffect {
    data class ShowLoadError(
        val error: Throwable,
        val onRetry: () -> Unit,
    ) : TitleScreenSideEffect
}
