package com.xbot.shared.ui.designsystem.utils

import org.jetbrains.compose.resources.PluralStringResource
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
    class String(val text: kotlin.String) : StringResource
    class Text(
        val resource: org.jetbrains.compose.resources.StringResource,
        vararg val formatArgs: Any,
    ) : StringResource
    class Plural(
        val resource: PluralStringResource,
        val quantity: Int,
        vararg val formatArgs: Any,
    ) : StringResource
}
