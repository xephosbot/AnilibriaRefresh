package com.xbot.home

sealed interface HomeScreenAction {
    data object Refresh : HomeScreenAction
    data class UpdateBestType(val bestType: BestType) : HomeScreenAction
    //TODO: Remove after Paging 3.5.0 use asState()
    data class ShowErrorMessage(
        val error: Throwable,
        val onRetry: () -> Unit,
    ) : HomeScreenAction
}
