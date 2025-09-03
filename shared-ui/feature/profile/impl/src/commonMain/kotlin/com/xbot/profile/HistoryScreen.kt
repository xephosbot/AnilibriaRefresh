package com.xbot.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.xbot.designsystem.components.Feed
import com.xbot.designsystem.components.PreferenceItem
import com.xbot.designsystem.components.ReleaseListItem
import com.xbot.designsystem.components.header
import com.xbot.designsystem.components.itemsIndexed
import com.xbot.designsystem.components.row
import com.xbot.designsystem.modifier.ProvideShimmer
import com.xbot.designsystem.modifier.shimmerUpdater
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HistoryScreen(
    modifier: Modifier = Modifier,
    viewModel: HistoryViewModel = koinViewModel(),
    contentPadding: PaddingValues,
    onReleaseClick: (Int) -> Unit = {}
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
        Feed(
            modifier = modifier.shimmerUpdater(shimmer),
            contentPadding = contentPadding,
            columns = GridCells.Adaptive(350.dp),
        ) {
            row {
                PreferenceItem(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .clip(RoundedCornerShape(
                            topStart = CornerSize(24.dp),
                            topEnd = CornerSize(24.dp),
                            bottomEnd = CornerSize(0.dp),
                            bottomStart = CornerSize(0.dp)
                        )),
                    title = "Импортировать историю",
                ) {

                }
            }
            row {
                Spacer(Modifier.height(DividerDefaults.Thickness))
            }
            row {
                PreferenceItem(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .clip(RoundedCornerShape(
                            topStart = CornerSize(0.dp),
                            topEnd = CornerSize(0.dp),
                            bottomEnd = CornerSize(24.dp),
                            bottomStart = CornerSize(24.dp)
                        )),
                    title = "Экспортировать историю",
                ) {

                }
            }

            header(
                title = { Text("История просмотра") },
            )
            itemsIndexed(items) { index, release ->
                Column {
                    ReleaseListItem(
                        modifier = Modifier.feedItemSpacing(index),
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