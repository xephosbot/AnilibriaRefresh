package com.xbot.designsystem.components

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.shimmer
import com.xbot.designsystem.modifier.LocalShimmer
import com.xbot.designsystem.modifier.fadedEdge
import com.xbot.designsystem.utils.PreviewContainer
import com.xbot.designsystem.utils.dummyReleaseList
import com.xbot.domain.models.Release
import com.xbot.localization.localizedName
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun MediumReleaseCard(
    release: Release?,
    onClick: (Release) -> Unit,
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource? = null,
    content: @Composable (ColumnScope.() -> Unit)? = null
) {
    Crossfade(
        modifier = Modifier
            .clip(RoundedCornerShape(24.dp))
            .clickable(
                interactionSource = interactionSource,
                indication = LocalIndication.current,
            ) {
                release?.let { onClick(it) }
            },
        targetState = release,
        label = "ReleaseCardItem Crossfade to ${if (release == null) "Loading" else "Loaded Release"}",
    ) { state ->
        when (state) {
            null -> MediumReleaseCardPlaceholder(modifier, content)
            else -> MediumReleaseCardContent(state, modifier, content)
        }
    }
}

@Composable
private fun MediumReleaseCardContent(
    release: Release,
    modifier: Modifier = Modifier,
    content: @Composable (ColumnScope.() -> Unit)?
) {
    MediumReleaseCardLayout(
        modifier = modifier,
        poster = {
            PosterImage(
                poster = release.poster,
                modifier = Modifier.fillMaxSize()
            )
        },
        title = {
            Text(
                text = release.localizedName(),
                style = MaterialTheme.typography.headlineLarge,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            ReleaseMetaText(release = release)
        },
        content = {
            content?.invoke(this)
        }
    )
}

@Composable
private fun MediumReleaseCardLayout(
    modifier: Modifier = Modifier,
    poster: @Composable () -> Unit,
    title: @Composable ColumnScope.() -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceContainer
    ) {
        Column(
            modifier = modifier
                .widthIn(max = ReleaseCardWidth),
        ) {
            Box(
                modifier = modifier,
                contentAlignment = Alignment.BottomStart
            ) {
                Box(
                    modifier = Modifier
                        .size(
                            width = ReleaseCardWidth,
                            height = ReleaseCardPosterHeight
                        )
                        .fadedEdge(edgeHeightRatio = 0.5f),
                ) {
                    poster()
                }
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    title()
                }
            }
            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
            ) {
                content()
            }
            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun MediumReleaseCardPlaceholder(
    modifier: Modifier = Modifier,
    content: @Composable (ColumnScope.() -> Unit)?
) {
    val shimmer = LocalShimmer.current

    MediumReleaseCardLayout(
        modifier = modifier,
        poster = {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .shimmer(shimmer)
                    .background(Color.LightGray)
                    .fadedEdge(edgeHeight = 350.dp)
            )
        },
        title = {},
        content = {
            content?.invoke(this)
        }
    )
}

@Preview
@Composable
private fun MediumReleaseCardPreview() {
    PreviewContainer {
        MediumReleaseCard(
            release = dummyReleaseList[0],
            onClick = {},
        ) {
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { /*TODO*/ },
            ) {
                Text(text = "Button")
            }
        }
    }
}

private val ReleaseCardWidth = 300.dp
private val ReleaseCardPosterHeight = 284.dp
