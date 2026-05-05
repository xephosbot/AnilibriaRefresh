package com.xbot.sharedapp

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveComponentOverrideApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.material3.adaptive.navigationsuite.LocalNavigationSuiteScaffoldWithPrimaryActionOverride
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteItem
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.rememberNavigationSuiteScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import com.xbot.designsystem.components.AnilibriaNavigationSuiteScaffold
import com.xbot.designsystem.components.NavigationSuiteScaffoldDefaults
import com.xbot.designsystem.theme.AnilibriaTheme
import com.xbot.domain.models.enums.ThemeOption
import com.xbot.home.navigation.HomeRoute
import com.xbot.localization.ProvideAppLocale
import com.xbot.navigation.LocalNavigator
import com.xbot.navigation.TopLevelRoutes
import com.xbot.navigation.rememberNavigator
import com.xbot.network.utils.ImageUrlProvider
import com.xbot.sharedapp.coil.ImageUrlMapper
import com.xbot.sharedapp.di.koinNavSerializersModule
import com.xbot.sharedapp.navigation.AnilibriaNavGraph
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@OptIn(
    ExperimentalMaterial3ExpressiveApi::class,
    ExperimentalMaterial3AdaptiveComponentOverrideApi::class
)
@Composable
internal fun AnilibriaApp(
    viewModel: AppViewModel = koinViewModel()
) {
    val imageUrlProvider = koinInject<ImageUrlProvider>()
    setSingletonImageLoaderFactory { context ->
        ImageLoader.Builder(context)
            .components {
                add(ImageUrlMapper(imageUrlProvider))
            }
            .build()
    }

    val navigator = rememberNavigator(
        startRoute = HomeRoute,
        topLevelRoutes = TopLevelRoutes,
        serializersModule = koinNavSerializersModule(),
        onInterceptNavigation = { key ->
            key
            //TODO: Implement navigator interception
        }
    )

    val appThemeState by viewModel.state.collectAsStateWithLifecycle()

    val darkTheme = when (appThemeState.themeOption) {
        ThemeOption.System -> isSystemInDarkTheme()
        ThemeOption.Dark -> true
        ThemeOption.Light -> false
    }

    CompositionLocalProvider(
        LocalNavigator provides navigator,
        LocalNavigationSuiteScaffoldWithPrimaryActionOverride provides AnilibriaNavigationSuiteScaffold
    ) {
        ProvideAppLocale {
            AnilibriaTheme(
                darkTheme = darkTheme,
                dynamicColor = appThemeState.isDynamicTheme,
                amoled = appThemeState.isPureBlack,
                expressiveColor = appThemeState.isExpressiveColor
            ) {
                val navigationSuiteScaffoldState = rememberNavigationSuiteScaffoldState()
                val navSuiteType =
                    NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(currentWindowAdaptiveInfoV2())

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
