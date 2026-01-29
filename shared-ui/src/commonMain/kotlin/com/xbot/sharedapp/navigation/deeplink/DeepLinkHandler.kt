package com.xbot.sharedapp.navigation.deeplink

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import com.xbot.common.navigation.NavKey
import com.xbot.login.navigation.LoginRoute
import com.xbot.title.navigation.TitleRoute
import io.ktor.http.Url

@Composable
internal fun DeepLinkListener(onUri: (String) -> Unit) {
    val currentOnUri by rememberUpdatedState(onUri)
    DisposableEffect(Unit) {
        ExternalUriHandler.listener = { uri ->
            currentOnUri.invoke(uri)
        }

        onDispose {
            ExternalUriHandler.listener = null
        }
    }
}

object ExternalUriHandler {

    private var cached: String? = null

    var listener: ((uri: String) -> Unit)? = null
        set(value) {
            field = value
            if(value != null) {
                cached?.let { cachedUrl ->
                    value.invoke(cachedUrl)
                }
                cached = null
            }
        }

    fun onNewUri(uri: String) {
        cached = uri
        listener?.let {
            it.invoke(uri)
            cached = null
        }
    }
}

internal val deepLinkPatterns = listOf(
    DeepLinkPattern(TitleRoute.serializer(), Url("https://aniliberty.top/anime/releases/release/{aliasOrId}")),
    DeepLinkPattern(TitleRoute.serializer(), Url("https://anilibria.top/anime/releases/release/{aliasOrId}")),
    DeepLinkPattern(TitleRoute.serializer(), Url("anilibria://release/{aliasOrId}")),
    DeepLinkPattern(LoginRoute.serializer(), Url("anilibria://login")),
)

internal fun parseDeepLink(uri: String): NavKey? {
    val url = try { Url(uri) } catch (e: Exception) { return null }
    val request = DeepLinkRequest(url)

    return deepLinkPatterns.firstNotNullOfOrNull { pattern ->
        checkMatch(request, pattern)
    }?.let { match ->
        try {
            match.serializer.deserialize(KeyDecoder(match.args))
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}

private fun <T : NavKey> checkMatch(request: DeepLinkRequest, pattern: DeepLinkPattern<T>): DeepLinkMatchResult<T>? {
    return DeepLinkMatcher(request, pattern).match()
}
