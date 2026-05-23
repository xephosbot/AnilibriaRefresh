package com.xbot.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalUriHandler
import androidx.navigation3.runtime.deeplink.DeepLinkRequest
import androidx.navigation3.runtime.deeplink.DeepLinkUri
import androidx.navigation3.runtime.deeplink.UriDeepLinkMatcher
import com.xbot.login.navigation.LoginRoute
import com.xbot.title.navigation.TitleRoute
import kotlinx.serialization.serializer

private val deepLinkMatchers = listOf(
    UriDeepLinkMatcher(
        uriPattern = DeepLinkUri("https://anilibria.tv/release/{aliasOrId}"),
        serializer = serializer<TitleRoute>()
    ),
    UriDeepLinkMatcher(
        uriPattern = DeepLinkUri("anilibria://release/{aliasOrId}"),
        serializer = serializer<TitleRoute>()
    ),
    UriDeepLinkMatcher(
        uriPattern = DeepLinkUri("anilibria://login"),
        serializer = serializer<LoginRoute>()
    )
)

/**
 * Parses a deep link [DeepLinkRequest] and returns the corresponding [NavKey].
 */
internal fun parseDeepLink(request: DeepLinkRequest): NavKey? {
    for (matcher in deepLinkMatchers) {
        val result = matcher.match(request)
        if (result != null) {
            return result.key
        }
    }
    return null
}

/**
 * A Composable that listens for deep links from [ExternalUriHandler] and triggers [onDeepLink].
 */
@Composable
fun DeepLinkListener(onDeepLink: (NavKey) -> Unit) {
    val uriHandler = LocalUriHandler.current
    val currentOnDeepLink by rememberUpdatedState(onDeepLink)

    DisposableEffect(Unit) {
        ExternalUriHandler.listener = { uri ->
            val navKey = parseDeepLink(uri)
            if (navKey != null) {
                currentOnDeepLink(navKey)
            } else {
                uriHandler.openUri(uri.uri.toString())
            }
        }

        onDispose {
            ExternalUriHandler.listener = null
        }
    }
}
