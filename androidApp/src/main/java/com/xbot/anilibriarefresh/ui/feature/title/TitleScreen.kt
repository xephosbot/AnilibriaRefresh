package com.xbot.anilibriarefresh.ui.feature.title

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.size.Size
import coil3.toBitmap
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.xbot.anilibriarefresh.models.Episode
import com.xbot.anilibriarefresh.models.Poster
import com.xbot.anilibriarefresh.models.TitleDetail
import com.xbot.anilibriarefresh.ui.components.OverlayHeaderLayout
import com.xbot.anilibriarefresh.ui.components.PosterImage
import com.xbot.anilibriarefresh.ui.components.rememberOverlayHeaderLayoutScrollBehavior
import com.xbot.anilibriarefresh.ui.icons.AnilibriaIcons
import com.xbot.anilibriarefresh.ui.icons.Heart
import com.xbot.designsystem.modifiers.fadedEdge
import com.xbot.anilibriarefresh.ui.utils.rememberBlurredBitmap
import com.xbot.designsystem.modifiers.ProvideShimmer
import com.xbot.designsystem.modifiers.shimmerSafe
import com.xbot.designsystem.modifiers.shimmerUpdater
import dev.chrisbanes.haze.HazeState
import org.koin.androidx.compose.koinViewModel

@Composable
fun TitleScreen(
    modifier: Modifier = Modifier,
    viewModel: TitleViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    if (state is TitleScreenState.Success) {
        TitleScreenContent(
            modifier = modifier,
            title = (state as TitleScreenState.Success).title,
        )
    }
}

@Composable
private fun TitleScreenContent(
    modifier: Modifier = Modifier,
    title: TitleDetail,
) {
    val scrollBehavior = rememberOverlayHeaderLayoutScrollBehavior()
    val hazeState = remember { HazeState() }

    OverlayHeaderLayout(
        modifier = modifier,
        headlineContent = {
            PosterWithBackground(
                modifier = Modifier
                    .graphicsLayer {
                        alpha = scrollBehavior.state.collapsedFraction
                    },
                poster = title.poster,
            )
        },
        scrollBehavior = scrollBehavior,
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
        ) {
            val shimmer = rememberShimmer(ShimmerBounds.Custom)

            ProvideShimmer(shimmer) {
                LazyColumn(
                    modifier = Modifier.shimmerUpdater(shimmer),
                ) {
                    mainBlock(
                        title = title
                    )
                    episodes(
                        items = title.episodes,
                        onItemClick = {}
                    )
                }
            }
        }
    }
}

@Composable
private fun PosterWithBackground(
    modifier: Modifier = Modifier,
    poster: Poster,
) {
    val posterPainter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(poster)
            .size(Size.ORIGINAL)
            .build(),
    )

    val posterImageLoadedState by posterPainter.state.collectAsStateWithLifecycle()

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(350.dp),
        contentAlignment = Alignment.Center,
    ) {
        Crossfade(targetState = posterImageLoadedState) { state ->
            when (state) {
                is AsyncImagePainter.State.Success -> {
                    val posterBitmap = state.result.image.toBitmap()
                    val blurredPosterBitmap = rememberBlurredBitmap(posterBitmap, radius = 25f)

                    Image(
                        modifier = Modifier
                            .fillMaxSize()
                            .fadedEdge(edgeHeight = 150.dp)
                            .fadedEdge(edgeHeight = 150.dp, bottomEdge = false),
                        bitmap = blurredPosterBitmap.asImageBitmap(),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                    )

                    Image(
                        modifier = Modifier
                            .fillMaxHeight()
                            .aspectRatio(7f / 10f)
                            .clip(RoundedCornerShape(16.dp))
                            .align(Alignment.Center),
                        bitmap = posterBitmap.asImageBitmap(),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                    )
                }
                else -> {
                    val shimmer = rememberShimmer(ShimmerBounds.View)

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .shimmerSafe(shimmer)
                            .fadedEdge(edgeHeight = 150.dp)
                            .fadedEdge(edgeHeight = 150.dp, bottomEdge = false)
                            .background(Color.LightGray),
                    )
                }
            }
        }
    }
}

private fun LazyListScope.mainBlock(
    title: TitleDetail
) {
    item {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp),
        ) {
            Spacer(Modifier.height(16.dp))
            Text(
                text = title.name,
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold,
            )
            Spacer(Modifier.height(16.dp))
            Row {
                Button(
                    modifier = Modifier.weight(3f),
                    onClick = {},
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = null,
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(text = "Смотреть")
                }
                Spacer(Modifier.width(16.dp))
                OutlinedButton(
                    modifier = Modifier.weight(1.5f),
                    onClick = {},
                ) {
                    Icon(
                        imageVector = AnilibriaIcons.Filled.Heart,
                        contentDescription = null,
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(text = "2.1k")
                }
            }
            var expanded by remember { mutableStateOf(false) }

            Box(
                modifier = Modifier
                    .clickable {
                        expanded = !expanded
                    }
                    .padding(16.dp),
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateContentSize(),
                    text = title.description,
                    maxLines = if (expanded) Int.MAX_VALUE else 3,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

private fun LazyListScope.episodes(
    items: List<Episode>,
    onItemClick: (Episode) -> Unit
) {
    items(
        items = items,
        key = { it.id }
    ) { episode ->
        EpisodeItem(
            episode = episode,
            onClick = { onItemClick(episode) }
        )
    }
}

@Composable
private fun EpisodeItem(
    modifier: Modifier = Modifier,
    episode: Episode,
    onClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
    ) {
        PosterImage(
            modifier = Modifier
                .height(100.dp)
                .width(150.dp)
                .clip(RoundedCornerShape(8.dp)),
            poster = episode.preview,
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = episode.name,
            fontWeight = FontWeight.Medium,
            fontSize = 18.sp,
        )
    }
}
