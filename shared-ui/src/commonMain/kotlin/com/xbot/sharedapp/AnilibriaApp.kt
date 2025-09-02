package com.xbot.sharedapp

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.rememberNavigationSuiteScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.map.Mapper
import coil3.memory.MemoryCache
import coil3.network.ktor3.KtorNetworkFetcherFactory
import coil3.request.CachePolicy
import coil3.request.crossfade
import com.xbot.designsystem.components.NavigationSuiteScaffoldDefaults
import com.xbot.designsystem.theme.AnilibriaTheme
import com.xbot.domain.models.Poster
import com.xbot.sharedapp.navigation.AnilibriaNavGraph
import com.xbot.sharedapp.navigation.hasRoute
import io.ktor.client.HttpClient
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

@Composable
internal fun AnilibriaApp(
    navigator: AnilibriaNavigator = rememberAnilibriaNavigator()
) {
    val httpClient: HttpClient = koinInject()

    setSingletonImageLoaderFactory { context ->
        ImageLoader.Builder(context)
            .crossfade(true)
            .components {
                add(
                    KtorNetworkFetcherFactory(
                        httpClient = { httpClient }
                    )
                )
                add(Mapper<Poster, String> { data, _ -> data.src })
            }
            .memoryCache {
                MemoryCache.Builder()
                    .maxSizePercent(context)
                    .build()
            }
            .diskCachePolicy(CachePolicy.ENABLED)
            .build()
    }

    AnilibriaTheme {
        val navigationSuiteScaffoldState = rememberNavigationSuiteScaffoldState()

        val currentDestination = navigator.currentDestination
        val currentTopLevelDestination = navigator.currentTopLevelDestination

        LaunchedEffect(currentDestination) {
            snapshotFlow { currentDestination }
                .onEach {
                    if (it != currentTopLevelDestination) {
                        //navigationSuiteScaffoldState.hide()
                    } else {
                        //navigationSuiteScaffoldState.show()
                    }
                }
                .collect()
        }

        NavigationSuiteScaffold(
            navigationSuiteItems = {
                AnilibriaNavigator.topLevelDestinations.forEach { destination ->
                    val isSelected = currentTopLevelDestination?.destination.hasRoute(destination.route)
                    item(
                        selected = isSelected,
                        icon = {
                            Icon(
                                imageVector = when (isSelected) {
                                    true -> destination.selectedIcon
                                    else -> destination.unselectedIcon
                                },
                                contentDescription = stringResource(destination.textRes),
                            )
                        },
                        onClick = {
                            navigator.navigate(destination.route, true)
                        },
                    )
                }
            },
            layoutType = NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(currentWindowAdaptiveInfo()),
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            navigationSuiteColors = NavigationSuiteDefaults.colors(
                navigationBarContainerColor = MaterialTheme.colorScheme.surface,
                navigationRailContainerColor = MaterialTheme.colorScheme.surface,
            ),
            state = navigationSuiteScaffoldState
        ) {
            AnilibriaNavGraph(
                navigator = navigator
            )
        }
    }
}
