package com.xbot.sharedapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveComponentOverrideApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteItem
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.rememberNavigationSuiteScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
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
import com.xbot.navigation.Navigator
import com.xbot.navigation.TopLevelRoutes
import com.xbot.navigation.rememberNavigator
import com.xbot.network.utils.ImageUrlProvider
import com.xbot.sharedapp.coil.ImageUrlMapper
import com.xbot.sharedapp.di.koinLazyInject
import com.xbot.sharedapp.di.koinNavSerializersModule
import com.xbot.sharedapp.navigation.AnilibertyNavGraph
import io.ktor.client.HttpClient
import kotlinx.coroutines.flow.distinctUntilChanged
import org.jetbrains.compose.resources.stringResource

/**
 * @param chromeHost when non-null, the platform owns the navigation chrome (the native tab bar on
 * iOS) and Compose renders the navigation graph alone. The navigation state itself always stays in
 * Navigation 3 — the host only receives updates and reports taps back.
 */
@OptIn(
    ExperimentalMaterial3ExpressiveApi::class,
    ExperimentalMaterial3AdaptiveComponentOverrideApi::class,
)
@Composable
internal fun AnilibertyApp(
    appState: AppState = rememberAnilibertyAppState(),
    chromeHost: NavigationChromeHost? = null,
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

    if (chromeHost != null) {
        DisposableEffect(chromeHost, navigator) {
            chromeHost.onTabSelected = { destination -> navigator.navigate(destination) }
            onDispose { chromeHost.onTabSelected = null }
        }
        LaunchedEffect(chromeHost, navigator) {
            snapshotFlow {
                navigator.currentTopLevelDestination to
                    (navigator.currentDestination?.hidesNavigationBar != true)
            }
                .distinctUntilChanged()
                .collect { (topLevel, chromeVisible) ->
                    chromeHost.onNavigationStateChanged(topLevel, chromeVisible)
                }
        }
    }

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
                if (chromeHost != null) {
                    AnilibertyNavGraph(navigator = navigator)
                } else {
                    ComposeNavigationChrome(navigator = navigator)
                }
            }
        }
    }
}

/**
 * The Compose-rendered navigation chrome, used on every platform that does not supply a
 * [NavigationChromeHost] of its own.
 */
@OptIn(
    ExperimentalMaterial3ExpressiveApi::class,
    ExperimentalMaterial3AdaptiveComponentOverrideApi::class,
)
@Composable
private fun ComposeNavigationChrome(navigator: Navigator) {
    val navigationSuiteScaffoldState = rememberNavigationSuiteScaffoldState()
    val navSuiteType =
        NavigationSuiteScaffoldDefaults.navigationSuiteType(currentWindowAdaptiveInfoV2())

    val currentTopLevelDestination = navigator.currentTopLevelDestination

    NavigationSuiteScaffold(
        navigationItems = {
            TopLevelRoutes.forEach { destination ->
                val isSelected = currentTopLevelDestination == destination

                NavigationSuiteItem(
                    selected = isSelected,
                    onClick = { navigator.navigate(destination) },
                    icon = {
                        Icon(
                            imageVector = if (isSelected) destination.selectedIcon else destination.unselectedIcon,
                            contentDescription = stringResource(destination.textRes),
                        )
                    },
                    label = { Text(stringResource(destination.textRes)) },
                    navigationSuiteType = navSuiteType,
                )
            }
        },
        navigationSuiteType = navSuiteType,
        navigationSuiteColors = NavigationSuiteDefaults.colors(
            shortNavigationBarContainerColor = MaterialTheme.colorScheme.surface,
            navigationBarContainerColor = MaterialTheme.colorScheme.surface,
        ),
        state = navigationSuiteScaffoldState,
        navigationItemVerticalArrangement = Arrangement.Center,
    ) {
        AnilibertyNavGraph(navigator = navigator)
    }
}
