package com.xbot.anilibriarefresh.ui.utils

import android.content.res.Resources
import androidx.annotation.PluralsRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext

@Composable
fun stringResource(stringResource: StringResource): String {
    val resources = resources()
    return resources.stringResource(stringResource)
}

/**
 * A composable function that returns the [Resources]. It will be recomposed when `Configuration`
 * gets updated.
 */
@Composable
@ReadOnlyComposable
internal fun resources(): Resources {
    LocalConfiguration.current
    return LocalContext.current.resources
}

fun Resources.stringResource(stringResource: StringResource): String {
    return when (stringResource) {
        is StringResource.String -> stringResource.text
        is StringResource.Text -> {
            if (stringResource.formatArgs.isEmpty()) {
                getString(stringResource.id)
            } else {
                getString(stringResource.id, *stringResource.formatArgs)
            }
        }
        is StringResource.Plurals -> {
            if (stringResource.formatArgs.isEmpty()) {
                getQuantityString(stringResource.id, stringResource.quantity)
            } else {
                getQuantityString(stringResource.id, stringResource.quantity, *stringResource.formatArgs)
            }
        }
    }
}

sealed interface StringResource {
    class String(val text: kotlin.String) : StringResource
    class Text(
        @StringRes val id: Int,
        vararg val formatArgs: Any,
    ) : StringResource
    class Plurals(
        @PluralsRes val id: Int,
        val quantity: Int,
        vararg val formatArgs: Any,
    ) : StringResource
}
