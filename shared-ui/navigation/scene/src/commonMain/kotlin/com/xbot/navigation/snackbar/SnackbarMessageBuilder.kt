package com.xbot.navigation.snackbar

import com.xbot.resources.StringResource
import io.github.ajiekcx.declarativeSnackbar.core.SnackbarComponent
import io.github.ajiekcx.declarativeSnackbar.core.SnackbarDuration
import io.github.ajiekcx.declarativeSnackbar.core.SnackbarMessage as SnackbarEnvelope

@DslMarker
annotation class SnackbarDsl

@SnackbarDsl
class SnackbarMessageBuilder internal constructor(
    private val text: StringResource,
) {
    private var actionLabel: StringResource? = null
    private var onAction: (() -> Unit)? = null
    private var duration: SnackbarDuration? = null

    fun action(label: StringResource, onAction: () -> Unit) {
        this.actionLabel = label
        this.onAction = onAction
    }

    fun duration(duration: SnackbarDuration) {
        this.duration = duration
    }

    internal fun build(onActionPerformed: () -> Unit): SnackbarEnvelope<SnackbarMessage> {
        val label = actionLabel
        val userAction = onAction
        val content = if (label != null && userAction != null) {
            SnackbarMessage.WithAction(
                text = text,
                actionLabel = label,
                onAction = {
                    userAction()
                    onActionPerformed()
                },
            )
        } else {
            SnackbarMessage.Plain(text)
        }
        val resolvedDuration = duration ?: when (content) {
            is SnackbarMessage.WithAction -> SnackbarDuration.Permanent
            is SnackbarMessage.Plain -> SnackbarDuration.Short
        }
        return SnackbarEnvelope(content = content, duration = resolvedDuration)
    }
}

fun SnackbarComponent<SnackbarMessage>.show(
    text: StringResource,
    block: SnackbarMessageBuilder.() -> Unit = {},
) {
    show(SnackbarMessageBuilder(text).apply(block).build(onActionPerformed = ::hide))
}
