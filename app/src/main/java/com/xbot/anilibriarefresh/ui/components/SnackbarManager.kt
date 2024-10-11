package com.xbot.anilibriarefresh.ui.components

import androidx.annotation.PluralsRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Stable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID

@Stable
data class Message(
    val id: Long,
    val title: MessageContent,
    val action: MessageAction? = null
)

data class MessageAction(
    @StringRes val textId: Int,
    val action: () -> Unit = {}
)

sealed class MessageContent {
    data class String(val value: kotlin.String) : MessageContent()
    data class Text(@StringRes val textId: Int) : MessageContent()
    data class Plurals(@PluralsRes val pluralsId: Int, val quantity: Int) : MessageContent()
}

object SnackbarManager {

    private val _messages: MutableStateFlow<List<Message>> = MutableStateFlow(emptyList())
    val messages: StateFlow<List<Message>> get() = _messages.asStateFlow()

    fun showMessage(title: MessageContent, action: MessageAction? = null) {
        val message = Message(
            id = UUID.randomUUID().mostSignificantBits,
            title = title,
            action = action
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
