package com.xbot.resources

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.PluralStringResource
import org.jetbrains.compose.resources.getPluralString
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.pluralStringResource

@Composable
fun stringResource(res: StringResource): String = when (res) {
    is StringResource.String -> res.text
    is StringResource.Text -> {
        stringResource(res.resource, *res.formatArgs)
    }
    is StringResource.Plural -> {
        pluralStringResource(res.resource, res.quantity, *res.formatArgs)
    }
}

suspend fun getString(res: StringResource): String = when (res) {
    is StringResource.String -> res.text
    is StringResource.Text -> {
        if (res.formatArgs.isEmpty()) {
            getString(res.resource)
        } else {
            getString(res.resource, *res.formatArgs)
        }
    }
    is StringResource.Plural -> {
        if (res.formatArgs.isEmpty()) {
            getPluralString(res.resource, res.quantity)
        } else {
            getPluralString(res.resource, res.quantity, *res.formatArgs)
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
        val resource: PluralStringResource,
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
