package com.xbot.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ThreePaneScaffoldPaneScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.xbot.designsystem.components.ChipGroup
import com.xbot.designsystem.components.Feed
import com.xbot.designsystem.components.ReleaseListItem
import com.xbot.designsystem.components.TopSearchInputField
import com.xbot.designsystem.components.header
import com.xbot.designsystem.components.pagingItems
import com.xbot.designsystem.icons.AnilibriaIcons
import com.xbot.designsystem.modifier.ProvideShimmer
import com.xbot.designsystem.modifier.shimmerUpdater
import com.xbot.designsystem.utils.union
import com.xbot.domain.models.Release
import com.xbot.resources.Res
import com.xbot.resources.button_filters
import com.xbot.resources.label_search_results
import com.xbot.resources.search_bar_placeholder
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
internal fun SearchResultPane(
    modifier: Modifier = Modifier,
    items: LazyPagingItems<Release>,
    searchFieldState: TextFieldState,
    onRetry: (Throwable, () -> Unit) -> Unit,
    onBackClick: () -> Unit,
    onShowFilters: () -> Unit,
    onReleaseClick: (Int) -> Unit,
) {
    val showErrorMessage: (Throwable) -> Unit = { error ->
        onRetry(error) { items.retry() }
    }

    LaunchedEffect(items) {
        snapshotFlow { items.loadState }.collect { loadState ->
            (loadState.refresh as? LoadState.Error)?.let { showErrorMessage(it.error) }
            (loadState.append as? LoadState.Error)?.let { showErrorMessage(it.error) }
            (loadState.prepend as? LoadState.Error)?.let { showErrorMessage(it.error) }
        }
    }

    val feedState = rememberLazyGridState()
    val shimmer = rememberShimmer(ShimmerBounds.Custom)

    Scaffold(
        modifier = modifier,
        topBar = {
            Column(
                modifier = Modifier.background(MaterialTheme.colorScheme.surfaceContainer)
            ) {
                TopSearchInputField(
                    modifier = Modifier.fillMaxWidth(),
                    state = searchFieldState,
                    onSearch = {},
                    placeholder = { Text(stringResource(Res.string.search_bar_placeholder)) },
                    leadingIcon = {
                        IconButton(
                            onClick = { onBackClick() }
                        ) {
                            Icon(
                                imageVector = AnilibriaIcons.Outlined.ArrowBack,
                                contentDescription = null
                            )
                        }
                    },
                    trailingIcon = {
                        IconButton(
                            onClick = { searchFieldState.clearText() }
                        ) {
                            Icon(
                                imageVector = AnilibriaIcons.Outlined.Clear,
                                contentDescription = null
                            )
                        }
                    },
                )
                ChipGroup {
                    AssistChip(
                        onClick = { onShowFilters() },
                        label = { Text(text = stringResource(Res.string.button_filters)) },
                        trailingIcon = {
                            Icon(
                                modifier = Modifier.size(AssistChipDefaults.IconSize),
                                imageVector = AnilibriaIcons.Outlined.ArrowDropDown,
                                contentDescription = null
                            )
                        }
                    )
                }
                Spacer(Modifier.height(8.dp))
                HorizontalDivider()
            }
        },
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
    ) { innerPadding ->
        ProvideShimmer(shimmer) {
            SearchResultContent(
                modifier = modifier.shimmerUpdater(shimmer),
                items = items,
                state = feedState,
                contentPadding = innerPadding,
                onReleaseClick = onReleaseClick,
            )
        }
    }
}

@Composable
private fun SearchResultContent(
    modifier: Modifier = Modifier,
    items: LazyPagingItems<Release>,
    state: LazyGridState,
    contentPadding: PaddingValues,
    onReleaseClick: (Int) -> Unit,
) {
    Feed(
        modifier = modifier,
        columns = GridCells.Adaptive(350.dp),
        contentPadding = contentPadding.union(WindowInsets.ime.asPaddingValues()),
        state = state,
    ) {
        header(
            title = { Text(text = stringResource(Res.string.label_search_results)) },
        )
        pagingItems(items) { index, release ->
            Column {
                ReleaseListItem(
                    modifier = Modifier.feedItemSpacing(index),
                    release = release,
                    onClick = { onReleaseClick(it.id) },
                )
                Spacer(Modifier.height(16.dp))
            }
        }
    }
}