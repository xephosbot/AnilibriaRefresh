package com.xbot.designsystem.utils

import androidx.compose.runtime.Stable
import com.xbot.resources.StringResource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class SnackbarBuilder internal constructor(
    private val manager: SnackbarManager
) {
    private var title: StringResource? = null
    private var actionTitle: StringResource? = null
    private var action: (() -> Unit)? = null

    fun setTitle(value: StringResource) = apply {
        title = value
    }

    fun setAction(
        title: StringResource,
        onClick: () -> Unit
    ) = apply {
        this.actionTitle = title
        this.action = onClick
    }

    @OptIn(ExperimentalUuidApi::class)
    fun show() {
        val message = Message(
            id = Uuid.random().toLongs { mostSignificantBits, _ -> mostSignificantBits },
            title = requireNotNull(title),
            action = actionTitle?.let {
                MessageAction(it, action ?: {})
            }
        )

        manager.enqueue(message)
    }
}

@OptIn(ExperimentalUuidApi::class)
object SnackbarManager {
    private val _messages: MutableStateFlow<List<Message>> = MutableStateFlow(emptyList())
    internal val messages: StateFlow<List<Message>> get() = _messages.asStateFlow()

    internal fun enqueue(message: Message) {
        _messages.update { it + message }
    }

    internal fun setMessageShown(messageId: Long) {
        _messages.update { currentMessages ->
            currentMessages.filterNot { it.id == messageId }
        }
    }
}

fun SnackbarManager.build(): SnackbarBuilder = SnackbarBuilder(this)

@Stable
internal data class Message(
    val id: Long,
    val title: StringResource,
    val action: MessageAction? = null,
)

@Stable
internal data class MessageAction(
    val title: StringResource,
    val action: () -> Unit = {},
)
