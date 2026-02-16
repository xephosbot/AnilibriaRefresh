package com.xbot.designsystem.utils

import org.jetbrains.compose.resources.getPluralString
import org.jetbrains.compose.resources.getString

suspend fun stringResource(stringResource: StringResource): String {
    return when (stringResource) {
        is StringResource.String -> stringResource.text
        is StringResource.Text -> {
            if (stringResource.formatArgs.isEmpty()) {
                getString(stringResource.resource)
            } else {
                getString(stringResource.resource, *stringResource.formatArgs)
            }
        }
        is StringResource.Plural -> {
            if (stringResource.formatArgs.isEmpty()) {
                getPluralString(stringResource.resource, stringResource.quantity)
            } else {
                getPluralString(stringResource.resource, stringResource.quantity, *stringResource.formatArgs)
            }
        }
    }
}

sealed interface StringResource {
    data class String(val text: kotlin.String) : StringResource
    
    class Text(
        val resource: org.jetbrains.compose.resources.StringResource,
        vararg val formatArgs: Any,
    ) : StringResource {
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
    ) : StringResource {
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
