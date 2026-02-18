package com.xbot.localization

import org.jetbrains.compose.resources.getPluralString
import org.jetbrains.compose.resources.getString

suspend fun stringResource(uiText: UiText): String {
    return when (uiText) {
        is UiText.String -> uiText.text
        is UiText.Text -> {
            if (uiText.formatArgs.isEmpty()) {
                getString(uiText.resource)
            } else {
                getString(uiText.resource, *uiText.formatArgs)
            }
        }
        is UiText.Plural -> {
            if (uiText.formatArgs.isEmpty()) {
                getPluralString(uiText.resource, uiText.quantity)
            } else {
                getPluralString(uiText.resource, uiText.quantity, *uiText.formatArgs)
            }
        }
    }
}

sealed interface UiText {
    data class String(val text: kotlin.String) : UiText
    
    class Text(
        val resource: org.jetbrains.compose.resources.StringResource,
        vararg val formatArgs: Any,
    ) : UiText {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other == null || this::class != other::class) return false

            other as Text

            if (resource != other.resource) return false
            if (!formatArgs.contentEquals(other.formatArgs)) return false

            return true
        }

        override fun hashCode(): Int {
            var result = resource.hashCode()
            result = 31 * result + formatArgs.contentHashCode()
            return result
        }
    }

    class Plural(
        val resource: org.jetbrains.compose.resources.PluralStringResource,
        val quantity: Int,
        vararg val formatArgs: Any,
    ) : UiText {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other == null || this::class != other::class) return false

            other as Plural

            if (resource != other.resource) return false
            if (quantity != other.quantity) return false
            if (!formatArgs.contentEquals(other.formatArgs)) return false

            return true
        }

        override fun hashCode(): Int {
            var result = resource.hashCode()
            result = 31 * result + quantity
            result = 31 * result + formatArgs.contentHashCode()
            return result
        }
    }
}
