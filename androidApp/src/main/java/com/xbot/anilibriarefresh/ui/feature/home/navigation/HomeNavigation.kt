package com.xbot.anilibriarefresh.ui.feature.home.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.xbot.anilibriarefresh.R
import com.xbot.anilibriarefresh.icons.AnilibriaIcons
import com.xbot.anilibriarefresh.icons.Search
import com.xbot.anilibriarefresh.navigation.Route
import com.xbot.anilibriarefresh.navigation.lifecycleIsResumed
import com.xbot.anilibriarefresh.ui.feature.home.search.FiltersScreen
import com.xbot.anilibriarefresh.ui.feature.home.feed.HomeFeedScreen
import com.xbot.anilibriarefresh.ui.feature.home.search.SearchResultScreen

fun NavGraphBuilder.homeSection(
    onReleaseClick: (Int) -> Unit
) {
    composable<Route.Home> { backStackEntry ->
        HomeNestedNavHost { releaseId ->
            if (backStackEntry.lifecycleIsResumed()) {
                onReleaseClick(releaseId)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeNestedNavHost(
    onReleaseClick: (Int) -> Unit
) {
    val nestedNavController = rememberNavController()

    var searchQuery by rememberSaveable { mutableStateOf("") }
    var searchBarExpanded by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.TopCenter
            ) {
                SearchBar(
                    inputField = {
                        SearchBarDefaults.InputField(
                            query = searchQuery,
                            onQueryChange = {
                                searchQuery = it
                            },
                            onSearch = {
                                searchBarExpanded = false
                                nestedNavController.navigate(Route.Home.SearchResult(searchQuery))
                            },
                            expanded = searchBarExpanded,
                            onExpandedChange = { searchBarExpanded = it },
                            placeholder = {
                                Text(text = stringResource(R.string.search_bar_placeholder))
                            },
                            leadingIcon = {
                                IconButton(onClick = {}) {
                                    Icon(
                                        imageVector = ImageVector.vectorResource(R.drawable.ic_anilibria),
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                    )
                                }
                            },
                            trailingIcon = {
                                Icon(
                                    imageVector = AnilibriaIcons.Outlined.Search,
                                    contentDescription = null
                                )
                            }
                        )
                    },
                    expanded = searchBarExpanded,
                    onExpandedChange = { searchBarExpanded = it },
                    content = {
                        FiltersScreen()
                    }
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.surfaceContainer
    ) { innerPadding ->
        NavHost(
            navController = nestedNavController,
            startDestination = Route.Home.Feed,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) {
            composable<Route.Home.Feed> {
                HomeFeedScreen(
                    contentPadding = innerPadding,
                    onReleaseClick = onReleaseClick
                )
            }
            composable<Route.Home.SearchResult> {
                SearchResultScreen(
                    contentPadding = innerPadding,
                    onReleaseClick = onReleaseClick
                )
            }
        }
    }
}