package com.xbot.sharedapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteItem
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.material3.adaptive.navigationsuite.rememberNavigationSuiteScaffoldState
import androidx.compose.material3.animateFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.compose.setSingletonImageLoaderFactory
import coil3.map.Mapper
import coil3.memory.MemoryCache
import coil3.network.ktor3.KtorNetworkFetcherFactory
import coil3.request.CachePolicy
import coil3.request.crossfade
import com.xbot.common.navigation.LocalResultEventBus
import com.xbot.common.navigation.ResultEventBus
import com.xbot.designsystem.components.NavigationSuiteScaffoldDefaults
import com.xbot.designsystem.icons.AnilibriaIcons
import com.xbot.designsystem.theme.AnilibriaTheme
import com.xbot.domain.models.Poster
import com.xbot.resources.Res
import com.xbot.resources.fab_search
import com.xbot.search.navigation.navigateToSearch
import com.xbot.sharedapp.navigation.AnilibriaNavGraph
import io.ktor.client.HttpClient
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
internal fun AnilibriaApp(
    navigator: AnilibriaNavigator = rememberAnilibriaNavigator()
) {
    val httpClient: HttpClient = koinInject()

    setSingletonImageLoaderFactory { context ->
        getImageLoader(context, httpClient)
    }

    val resultBus = remember { ResultEventBus() }

    CompositionLocalProvider(LocalResultEventBus.provides(resultBus)) {
        AnilibriaTheme {
            val navigationSuiteScaffoldState = rememberNavigationSuiteScaffoldState()
            val navSuiteType =
                NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(currentWindowAdaptiveInfo())

            val currentDestination = navigator.currentDestination
            val currentTopLevelDestination = navigator.currentTopLevelDestination

            val fabModifier = if (
                navSuiteType == NavigationSuiteType.ShortNavigationBarMedium ||
                navSuiteType == NavigationSuiteType.ShortNavigationBarCompact
            ) {
                Modifier.animateFloatingActionButton(
                    visible = currentDestination == currentTopLevelDestination,
                    alignment = Alignment.Center
                )
            } else {
                Modifier
            }
            val fab = @Composable {
                val startPadding =
                    if (navSuiteType == NavigationSuiteType.ShortNavigationBarMedium) 0.dp else 20.dp
                ExtendedFloatingActionButton(
                    modifier = Modifier
                        .padding(start = startPadding)
                        .then(fabModifier),
                    onClick = { navigator.navigateToSearch() },
                    expanded = navSuiteType == NavigationSuiteType.WideNavigationRailExpanded,
                    icon = {
                        Icon(
                            imageVector = AnilibriaIcons.Outlined.Search,
                            contentDescription = null
                        )
                    },
                    text = { Text(stringResource(Res.string.fab_search)) }
                )
            }
            val fabMovable = remember(fab) { movableContentOf(fab) }

            NavigationSuiteScaffold(
                navigationItems = {
                    AnilibriaNavigator.topLevelDestinations.forEach { destination ->
                        val isSelected = currentTopLevelDestination == destination

                        NavigationSuiteItem(
                            selected = isSelected,
                            onClick = { navigator.navigate(destination) },
                            icon = {
                                Icon(
                                    imageVector = when (isSelected) {
                                        true -> destination.selectedIcon
                                        else -> destination.unselectedIcon
                                    },
                                    contentDescription = stringResource(destination.textRes),
                                )
                            },
                            label = { Text(stringResource(destination.textRes)) },
                            navigationSuiteType = navSuiteType,
                        )
                    }
                },
                primaryActionContent = fabMovable,
                navigationSuiteType = navSuiteType,
                navigationSuiteColors = NavigationSuiteDefaults.colors(
                    shortNavigationBarContainerColor = MaterialTheme.colorScheme.surface,
                    navigationBarContainerColor = MaterialTheme.colorScheme.surface,
                ),
                state = navigationSuiteScaffoldState,
                navigationItemVerticalArrangement = Arrangement.Center
            ) {
                AnilibriaNavGraph(
                    navigator = navigator
                )
            }
        }
    }
}

private fun getImageLoader(
    context: PlatformContext,
    httpClient: HttpClient
): ImageLoader = ImageLoader.Builder(context)
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
