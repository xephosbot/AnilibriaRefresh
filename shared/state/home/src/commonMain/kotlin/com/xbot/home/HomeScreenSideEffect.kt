package com.xbot.home

import com.xbot.domain.models.DomainError

sealed interface HomeScreenSideEffect {
    data class ShowErrorMessage(
        val error: DomainError,
        val onRetry: () -> Unit,
    ) : HomeScreenSideEffect
}
