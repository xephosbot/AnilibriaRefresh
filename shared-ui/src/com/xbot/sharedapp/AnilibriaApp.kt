package com.xbot.sharedapp

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteDefaults
import androidx.compose.material3.adaptive.navigationsuite.rememberNavigationSuiteScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.map.Mapper
import coil3.memory.MemoryCache
import coil3.request.CachePolicy
import coil3.request.crossfade
import com.xbot.designsystem.components.NavigationSuiteScaffold
import com.xbot.designsystem.theme.AnilibriaTheme
import com.xbot.domain.models.Poster
import com.xbot.sharedapp.navigation.AnilibriaNavGraph
import com.xbot.sharedapp.navigation.hasRoute
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import org.jetbrains.compose.resources.stringResource

@Composable
fun AnilibriaApp(
    appState: AnilibriaAppState = rememberAnilibriaAppState()
) {
    setSingletonImageLoaderFactory { context ->
        ImageLoader.Builder(context)
            .crossfade(true)
            .components {
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
        val currentDestination = appState.currentDestination
        val currentTopLevelDestination = appState.currentTopLevelDestination

        LaunchedEffect(currentTopLevelDestination) {
            snapshotFlow { currentTopLevelDestination }
                .onEach {
                    if (it != null) {
                        navigationSuiteScaffoldState.show()
                    } else {
                        navigationSuiteScaffoldState.hide()
                    }
                }
                .collect()
        }

        NavigationSuiteScaffold(
            navigationSuiteItems = {
                appState.topLevelDestinations.forEach { destination ->
                    val isSelected = currentDestination.hasRoute(destination.route)
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
                            appState.navigateToTopLevelDestination(destination)
                        },
                    )
                }
            },
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            navigationSuiteColors = NavigationSuiteDefaults.colors(
                navigationBarContainerColor = MaterialTheme.colorScheme.surface,
                navigationRailContainerColor = MaterialTheme.colorScheme.surface,
            ),
            state = navigationSuiteScaffoldState
        ) {
            AnilibriaNavGraph(
                appState = appState
            )
        }
    }
}
