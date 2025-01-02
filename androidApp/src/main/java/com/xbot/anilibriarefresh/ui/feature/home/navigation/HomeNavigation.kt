package com.xbot.anilibriarefresh.ui.feature.home.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.xbot.anilibriarefresh.R
import com.xbot.anilibriarefresh.icons.AnilibriaIcons
import com.xbot.anilibriarefresh.icons.Search
import com.xbot.anilibriarefresh.navigation.Route
import com.xbot.anilibriarefresh.navigation.lifecycleIsResumed
import com.xbot.anilibriarefresh.ui.feature.home.feed.HomeFeedScreen
import com.xbot.anilibriarefresh.ui.feature.home.search.FiltersScreen
import com.xbot.anilibriarefresh.ui.feature.home.search.SearchResultScreen
import soup.compose.material.motion.animation.materialFadeThroughIn
import soup.compose.material.motion.animation.materialFadeThroughOut

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

@Composable
private fun HomeNestedNavHost(
    onReleaseClick: (Int) -> Unit
) {
    val nestedNavController = rememberNavController()

    var searchQuery by rememberSaveable { mutableStateOf("") }
    var searchBarExpanded by rememberSaveable { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .semantics { traversalIndex = 0f }
    ) {
        val contentInsets = WindowInsets.statusBars.add(WindowInsets(top = 64.dp))

        NavHost(
            navController = nestedNavController,
            startDestination = Route.Home.Feed,
            enterTransition = { materialFadeThroughIn() },
            exitTransition = { materialFadeThroughOut() },
        ) {
            composable<Route.Home.Feed>(
                exitTransition = { ExitTransition.None }
            ) {
                HomeFeedScreen(
                    contentPadding = contentInsets.asPaddingValues(),
                    onReleaseClick = onReleaseClick
                )
            }
            composable<Route.Home.SearchResult>(
                enterTransition = { EnterTransition.None }
            ) {
                SearchResultScreen(
                    contentPadding = contentInsets.asPaddingValues(),
                    onReleaseClick = onReleaseClick
                )
            }
        }

        AnilibriaSearchBar(
            modifier = Modifier.align(Alignment.TopCenter),
            query = searchQuery,
            onQueryChange = { searchQuery = it },
            expanded = searchBarExpanded,
            onExpandedChange = { searchBarExpanded = it },
            onSearch = { query ->
                searchBarExpanded = false
                nestedNavController.navigate(Route.Home.SearchResult(query)) {
                    popUpTo<Route.Home.SearchResult> { inclusive = true }
                }
            }
        ) {
            FiltersScreen()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AnilibriaSearchBar(
    modifier: Modifier = Modifier,
    query: String,
    onQueryChange: (String) -> Unit,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onSearch: (String) -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    SearchBar(
        modifier = modifier,
        inputField = {
            SearchBarDefaults.InputField(
                query = query,
                onQueryChange = onQueryChange,
                onSearch = onSearch,
                expanded = expanded,
                onExpandedChange = onExpandedChange,
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
        expanded = expanded,
        onExpandedChange = onExpandedChange,
        content = content
    )
}