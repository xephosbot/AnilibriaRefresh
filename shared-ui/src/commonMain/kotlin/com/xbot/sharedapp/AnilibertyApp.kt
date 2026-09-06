package com.xbot.sharedapp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.network.ktor3.KtorNetworkFetcherFactory
import com.xbot.common.state.AppState
import com.xbot.common.state.LocalAppState
import com.xbot.designsystem.theme.AnilibertyTheme
import com.xbot.domain.models.AuthState
import com.xbot.home.navigation.HomeRoute
import com.xbot.localization.ProvideAppLocale
import com.xbot.login.navigation.LoginRoute
import com.xbot.navigation.LocalNavigator
import com.xbot.navigation.TopLevelRoutes
import com.xbot.navigation.rememberNavigator
import com.xbot.network.utils.ImageUrlProvider
import com.xbot.sharedapp.coil.ImageUrlMapper
import com.xbot.sharedapp.di.koinLazyInject
import com.xbot.sharedapp.di.koinNavSerializersModule
import com.xbot.sharedapp.navigation.AnilibertyNavGraph
import com.xbot.sharedapp.navigation.LocalNavigationChrome
import io.ktor.client.HttpClient

@Composable
internal fun AnilibertyApp(
    appState: AppState = rememberAnilibertyAppState(),
) {
    val imageUrlProvider = koinLazyInject<ImageUrlProvider>()
    val httpClient = koinLazyInject<HttpClient>()

    setSingletonImageLoaderFactory { context ->
        ImageLoader.Builder(context)
            .components {
                add(ImageUrlMapper(imageUrlProvider.value))
                add(KtorNetworkFetcherFactory(httpClient = { httpClient.value }))
            }
            .build()
    }

    val navigator = rememberNavigator(
        startRoute = HomeRoute,
        topLevelRoutes = TopLevelRoutes,
        serializersModule = koinNavSerializersModule(),
        onInterceptNavigation = { key ->
            if (key.requiresLogin && appState.authState !is AuthState.Authenticated) {
                LoginRoute(returnTo = key)
            } else {
                key
            }
        }
    )

    CompositionLocalProvider(
        LocalAppState provides appState,
        LocalNavigator provides navigator,
    ) {
        ProvideAppLocale {
            AnilibertyTheme(
                darkTheme = appState.themeState.isDarkTheme,
                dynamicColor = appState.themeState.isDynamicTheme,
                amoled = appState.themeState.isPureBlack,
                expressiveColor = appState.themeState.isExpressiveColor
            ) {
                // The chrome wraps the graph rather than the other way round, and it is chosen by
                // the platform, not by this composable: see LocalNavigationChrome.
                LocalNavigationChrome.current.Content(navigator) {
                    AnilibertyNavGraph(navigator = navigator)
                }
            }
        }
    }
}
