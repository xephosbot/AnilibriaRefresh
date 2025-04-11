package com.xbot.shared.ui.designsystem.utils

import androidx.compose.runtime.Stable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Stable
data class Message(
    val id: Long,
    val title: StringResource,
    val action: MessageAction? = null,
)

data class MessageAction(
    val title: StringResource,
    val action: () -> Unit = {},
)

@OptIn(ExperimentalUuidApi::class)
object SnackbarManager {
    private val _messages: MutableStateFlow<List<Message>> = MutableStateFlow(emptyList())
    val messages: StateFlow<List<Message>> get() = _messages.asStateFlow()

    fun showMessage(title: StringResource, action: MessageAction? = null) {
        val message = Message(
            id = Uuid.random().toLongs { mostSignificantBits, _ -> mostSignificantBits },
            title = title,
            action = action,
        )
        _messages.update { currentMessages ->
            currentMessages + message
        }
    }

    fun setMessageShown(messageId: Long) {
        _messages.update { currentMessages ->
            currentMessages.filterNot { it.id == messageId }
        }
    }
}
