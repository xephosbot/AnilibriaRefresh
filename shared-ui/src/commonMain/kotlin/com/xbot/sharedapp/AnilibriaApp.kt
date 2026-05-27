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
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.network.ktor3.KtorNetworkFetcherFactory
import com.xbot.common.state.AppState
import com.xbot.common.state.LocalAppState
import com.xbot.designsystem.theme.AnilibriaTheme
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
import com.xbot.sharedapp.navigation.AnilibriaNavGraph
import io.ktor.client.HttpClient
import org.jetbrains.compose.resources.stringResource

@OptIn(
    ExperimentalMaterial3ExpressiveApi::class,
    ExperimentalMaterial3AdaptiveComponentOverrideApi::class,
)
@Composable
internal fun AnilibriaApp(
    appState: AppState = rememberAnilibriaAppState()
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
            AnilibriaTheme(
                darkTheme = appState.themeState.isDarkTheme,
                dynamicColor = appState.themeState.isDynamicTheme,
                amoled = appState.themeState.isPureBlack,
                expressiveColor = appState.themeState.isExpressiveColor
            ) {
                val navigationSuiteScaffoldState = rememberNavigationSuiteScaffoldState()
                val navSuiteType =
                    NavigationSuiteScaffoldDefaults.navigationSuiteType(currentWindowAdaptiveInfoV2())

                val currentDestination = navigator.currentDestination
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
                    navigationItemVerticalArrangement = Arrangement.Center
                ) {
                    AnilibriaNavGraph(navigator = navigator)
                }
            }
        }
    }
}
