package com.xbot.preference.history

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.xbot.designsystem.components.ReleaseListItem
import com.xbot.designsystem.components.itemsIndexed
import com.xbot.designsystem.components.section
import com.xbot.designsystem.icons.AnilibriaIcons
import com.xbot.designsystem.modifier.ProvideShimmer
import com.xbot.designsystem.modifier.shimmerUpdater
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun ViewHistoryDetailScreen(
    modifier: Modifier = Modifier,
    viewModel: HistoryViewModel = koinViewModel(),
    contentPadding: PaddingValues = PaddingValues(),
    onReleaseClick: (Int) -> Unit = {},
    onNavigateBack: () -> Unit = {},
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    HistoryScreenContent(
        modifier = modifier,
        state = state,
        contentPadding = contentPadding,
        onReleaseClick = onReleaseClick
    )
}

@Composable
private fun HistoryScreenContent(
    modifier: Modifier = Modifier,
    state: HistoryScreenState,
    contentPadding: PaddingValues,
    onReleaseClick: (Int) -> Unit
) {
    val shimmer = rememberShimmer(ShimmerBounds.Custom)
    val items = (state as? HistoryScreenState.Success)?.releasesFeed?.recommendedReleases ?: List(10) { null }

    ProvideShimmer(shimmer) {
        LazyColumn(
            modifier = modifier.shimmerUpdater(shimmer),
            contentPadding = contentPadding,
        ) {
            section {
                item {
                    ListItem(
                        modifier = Modifier.clickable { },
                        headlineContent = {
                            Text(text = "Импортировать историю")
                        },
                        leadingContent = {
                            Icon(imageVector = AnilibriaIcons.Filled.Star, contentDescription = null)
                        },
                        colors = ListItemDefaults.colors(MaterialTheme.colorScheme.surfaceBright)
                    )
                }
                item {
                    ListItem(
                        modifier = Modifier.clickable { },
                        headlineContent = {
                            Text(text = "Экспортировать историю")
                        },
                        leadingContent = {
                            Icon(imageVector = AnilibriaIcons.Filled.Star, contentDescription = null)
                        },
                        colors = ListItemDefaults.colors(MaterialTheme.colorScheme.surfaceBright)
                    )
                }
            }

            section {
                itemsIndexed(items) { index, release ->
                    Column {
                        ReleaseListItem(
                            release = release,
                            onClick = { onReleaseClick(it.id) },
                        )
                        if (index < items.size - 1) {
                            Spacer(Modifier.height(16.dp))
                        }
                    }
                }
            }
        }
    }
}