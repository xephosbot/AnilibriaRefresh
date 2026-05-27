package com.xbot.navigation.snackbar

import androidx.compose.runtime.Immutable
import com.xbot.resources.StringResource

@Immutable
sealed interface SnackbarMessage {
    val text: StringResource

    data class Plain(
        override val text: StringResource,
    ) : SnackbarMessage

    data class WithAction(
        override val text: StringResource,
        val actionLabel: StringResource,
        val onAction: () -> Unit,
    ) : SnackbarMessage
}
