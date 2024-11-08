package com.xbot.anilibriarefresh.ui.feature.title

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.skydoves.cloudy.cloudy
import com.xbot.anilibriarefresh.ui.components.OverlayHeaderLayout
import com.xbot.anilibriarefresh.ui.components.PosterImage
import com.xbot.anilibriarefresh.ui.components.rememberOverlayHeaderLayoutScrollBehavior
import com.xbot.anilibriarefresh.ui.icons.AnilibriaIcons
import com.xbot.anilibriarefresh.ui.icons.Heart
import com.xbot.anilibriarefresh.ui.theme.AnilibriaTheme
import com.xbot.anilibriarefresh.ui.utils.fadedEdge
import com.xbot.anilibriarefresh.ui.utils.only
import com.xbot.domain.model.EpisodeModel
import com.xbot.domain.model.TitleDetailModel

@Composable
fun TitleScreenTest(
    modifier: Modifier = Modifier,
    viewModel: TitleViewModel = hiltViewModel(),
    paddingValues: PaddingValues
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    if (state is TitleScreenState.Success) {
        TitleScreenContent(
            modifier = modifier,
            title = (state as TitleScreenState.Success).title,
            paddingValues = paddingValues
        )
    }
}

@Composable
private fun TitleScreenContent(
    modifier: Modifier = Modifier,
    title: TitleDetailModel,
    paddingValues: PaddingValues
) {
    val scrollBehavior = rememberOverlayHeaderLayoutScrollBehavior()

    OverlayHeaderLayout(
        modifier = modifier,
        headlineContent = {
            Box(
                modifier = Modifier
                    .padding(paddingValues.only(WindowInsetsSides.Top))
                    .fillMaxWidth()
                    .height(300.dp)
                    .graphicsLayer {
                        alpha = scrollBehavior.state.collapsedFraction
                    },
                contentAlignment = Alignment.Center
            ) {
                PosterImage(
                    modifier = Modifier
                        .fillMaxSize()
                        .fadedEdge(edgeHeight = 200.dp)
                        .fadedEdge(edgeHeight = 200.dp, bottomEdge = false)
                        .cloudy(radius = 500),
                    poster = title.poster
                )
                PosterImage(
                    modifier = Modifier
                        .fillMaxHeight()
                        .aspectRatio(7f / 10f)
                        .clip(RoundedCornerShape(16.dp))
                        .shadow(elevation = 3.dp),
                    poster = title.poster,
                    contentScale = ContentScale.Fit
                )
            }
        },
        scrollBehavior = scrollBehavior
    ) {
        Surface {
            LazyColumn(
                contentPadding = paddingValues.only(WindowInsetsSides.Bottom)
            ) {
                item {
                    Column(
                        modifier = Modifier.padding(horizontal = 16.dp)
                    ) {
                        Spacer(Modifier.height(16.dp))
                        Text(
                            text = title.name,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(Modifier.height(16.dp))
                        Row {
                            Button(
                                modifier = Modifier.weight(1f),
                                onClick = {}
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = null
                                )
                                Spacer(Modifier.width(8.dp))
                                Text(text = "Смотреть")
                            }
                            Spacer(Modifier.width(16.dp))
                            OutlinedButton (
                                onClick = {}
                            ) {
                                Icon(
                                    imageVector = AnilibriaIcons.Filled.Heart,
                                    contentDescription = null
                                )
                                Spacer(Modifier.width(8.dp))
                                Text(text = "2.1k")
                            }
                        }
                        var expanded by remember { mutableStateOf(false) }

                        Box (
                            modifier = Modifier
                                .clickable {
                                    expanded = !expanded
                                }
                                .padding(16.dp)
                        ) {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .animateContentSize(),
                                text = title.description,
                                maxLines = if (expanded) Int.MAX_VALUE else 3,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
                items(title.episodes) { episode ->
                    EpisodeItem(
                        episode = episode,
                        onClick = {}
                    )
                }
            }
        }
    }
}

@Composable
private fun EpisodeItem(
    modifier: Modifier = Modifier,
    episode: EpisodeModel,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        PosterImage(
            modifier = Modifier
                .height(100.dp)
                .width(150.dp)
                .clip(RoundedCornerShape(8.dp)),
            poster = episode.preview
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = "Серия ${episode.ordinal}" + if (episode.name != null) " • ${episode.name}" else "",
            fontWeight = FontWeight.Medium,
            fontSize = 18.sp
        )
    }
}