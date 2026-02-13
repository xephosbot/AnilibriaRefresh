package com.xbot.designsystem.components

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.Hyphens
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.valentinilk.shimmer.shimmer
import com.xbot.designsystem.modifier.LocalShimmer
import com.xbot.designsystem.modifier.fadedEdge
import com.xbot.designsystem.theme.ExpressiveShape
import com.xbot.designsystem.theme.MorphingExpressiveShape
import com.xbot.designsystem.utils.AnilibriaPreview
import com.xbot.domain.models.Franchise
import com.xbot.domain.models.Release
import com.xbot.fixtures.data.franchiseMocks
import com.xbot.localization.localizedName
import com.xbot.resources.Res
import com.xbot.resources.franchise_episodes_count
import com.xbot.resources.franchise_seasons_count
import org.jetbrains.compose.resources.stringResource

@Composable
fun FranchiseCard(
    franchise: Franchise?,
    onClick: (Franchise) -> Unit,
    modifier: Modifier = Modifier,
    shape: ExpressiveShape = ExpressiveFranchiseCardDefaults.shape(),
    interactionSource: MutableInteractionSource? = null,
) {
    @Suppress("NAME_SHADOWING")
    val interactionSource = interactionSource ?: remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()

    Crossfade(
        modifier = modifier,
        targetState = franchise,
        label = "FranchiseCard Crossfade to ${if (franchise == null) "Loading" else "Loaded"}",
    ) { state ->
        when (state) {
            null -> FranchiseCardPlaceholder()
            else -> FranchiseCardContent(
                state,
                Modifier
                    .clip(shape.shapeForInteraction(pressed, false))
                    .clickable(
                    interactionSource = interactionSource,
                    indication = LocalIndication.current,
                ) { franchise?.let { onClick(it) }}
            )
        }
    }
}

@Composable
private fun FranchiseCardContent(
    franchise: Franchise,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = Modifier
            .width(FranchiseCardWidth)
            .height(FranchiseCardWidth + 20.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Box(
            modifier = Modifier
                .size(FranchiseCardWidth)
                .then(modifier)
                .fadedEdge(
                    startFraction = 0.25f,
                    endFraction = 1.0f,
                )
        ) {
            PosterImage(
                poster = franchise.poster,
                modifier = Modifier.fillMaxSize()
            )
        }

        Column(
            modifier = Modifier
                .width(FranchiseCardWidth)
                .height(FranchiseCardWidth + 20.dp), // Height to anchor text bottom at 300dp
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextAutoSize(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                text = franchise.localizedName(),
                autoSize = TextAutoSize.StepBased(
                    maxFontSize = MaterialTheme.typography.headlineLarge.fontSize,
                    minFontSize = MaterialTheme.typography.headlineSmall.fontSize,
                ),
                style = MaterialTheme.typography.headlineLarge
                    .copy(
                        lineBreak = LineBreak.Paragraph,
                        hyphens = Hyphens.Auto
                    ),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )

            Spacer(Modifier.height(12.dp))

            Surface(
                color = MaterialTheme.colorScheme.surfaceContainerHighest,
                shape = MaterialTheme.shapes.large
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Releases overlapping circles
                    if (!franchise.franchiseReleases.isNullOrEmpty()) {
                        OverlappingReleases(franchise.franchiseReleases!!)
                    }

                    Column(
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Text(
                            text = stringResource(Res.string.franchise_seasons_count, franchise.totalReleases),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = stringResource(Res.string.franchise_episodes_count, franchise.totalEpisodes ?: 0),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun OverlappingReleases(
    releases: List<Release>,
    maxVisible: Int = 2,
    size: Dp = 64.dp,
    overlap: Dp = 32.dp,
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainerHighest,
) {
    val displayCount = minOf(releases.size, maxVisible)
    val extraCount = releases.size - displayCount
    val totalWidth =
        size * displayCount - overlap * (displayCount - 1) + if (extraCount > 0) size - overlap else 0.dp

    Box(modifier = Modifier.width(totalWidth).height(size)) {
        for (i in 0 until displayCount) {
            Box(
                modifier = Modifier
                    .offset(x = (size - overlap) * i)
                    .zIndex(i.toFloat())
                    .size(size)
                    .background(containerColor, CircleShape)
                    .padding(4.dp)
                    .clip(CircleShape)
            ) {
                PosterImage(
                    poster = releases[i].poster,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        if (extraCount > 0) {
            Box(
                modifier = Modifier
                    .offset(x = (size - overlap) * displayCount)
                    .zIndex(displayCount.toFloat())
                    .size(size)
                    .background(containerColor, CircleShape)
                    .padding(4.dp)
                    .background(MaterialTheme.colorScheme.primary, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "+$extraCount",
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

@Composable
private fun FranchiseCardPlaceholder(
    modifier: Modifier = Modifier
) {
    val shimmer = LocalShimmer.current
    Box(
        modifier = Modifier
            .width(FranchiseCardWidth)
            .height(FranchiseCardWidth + 20.dp)
            .then(modifier),
        contentAlignment = Alignment.TopCenter
    ) {
        Box(
            modifier = Modifier
                .size(FranchiseCardWidth)
                .clip(CircleShape)
                .shimmer(shimmer)
                .background(Color.LightGray)
        )

        Column(
            modifier = Modifier
                .width(FranchiseCardWidth)
                .height(FranchiseCardWidth + 20.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .width(180.dp)
                    .height(32.dp)
                    .clip(MaterialTheme.shapes.small)
                    .shimmer(shimmer)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            )

            Spacer(Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(72.dp)
                    .padding(4.dp)
                    .clip(MaterialTheme.shapes.large)
                    .background(MaterialTheme.colorScheme.surfaceContainerHighest)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
object ExpressiveFranchiseCardDefaults {
    @Composable
    fun shape(): ExpressiveShape {
        return MorphingExpressiveShape(
            shape = MaterialShapes.Circle,
            pressedShape = MaterialShapes.Cookie12Sided,
            animationSpec =  MaterialTheme.motionScheme.defaultSpatialSpec(),
        )
    }
}

@Preview
@Composable
private fun FranchiseCardPreview() {
    AnilibriaPreview {
        FranchiseCard(
            franchise = franchiseMocks.first(),
            onClick = {}
        )
    }
}

@Preview
@Composable
private fun FranchiseCardPlaceholderPreview() {
    AnilibriaPreview {
        FranchiseCardPlaceholder()
    }
}

private val FranchiseCardWidth = 280.dp
