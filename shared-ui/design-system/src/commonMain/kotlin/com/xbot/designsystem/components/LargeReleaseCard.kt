package com.xbot.designsystem.components

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SplitButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowSizeClass.Companion.HEIGHT_DP_MEDIUM_LOWER_BOUND
import androidx.window.core.layout.WindowSizeClass.Companion.WIDTH_DP_MEDIUM_LOWER_BOUND
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer
import com.xbot.designsystem.icons.AnilibriaIcons
import com.xbot.designsystem.modifier.LocalShimmer
import com.xbot.designsystem.modifier.ProvideShimmer
import com.xbot.designsystem.modifier.fadedEdge
import com.xbot.designsystem.theme.AnilibriaTheme
import com.xbot.designsystem.utils.dummyReleaseList
import com.xbot.domain.models.Release
import com.xbot.localization.localizedName
import com.xbot.resources.Res
import com.xbot.resources.button_watch
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun LargeReleaseCard(
    release: Release?,
    modifier: Modifier = Modifier,
    contentModifier: Modifier = Modifier,
    content: @Composable (ColumnScope.() -> Unit)? = null
) {
    Crossfade(targetState = release) { state ->
        when (state) {
            null -> LargeReleaseCardPlaceholder(
                modifier = modifier
            )

            else -> LargeReleaseCardContent(
                modifier = modifier,
                contentModifier = contentModifier,
                release = state,
                content = content
            )
        }
    }
}

@Composable
private fun LargeReleaseCardContent(
    release: Release,
    modifier: Modifier = Modifier,
    contentModifier: Modifier = Modifier,
    content: @Composable (ColumnScope.() -> Unit)?
) {
    LargeReleaseCardLayout(
        modifier = Modifier,
        contentModifier = contentModifier,
        poster = {
            PosterImage(
                poster = release.poster,
                modifier = modifier.fillMaxSize()
            )
        },
        content = {
            Text(
                text = release.localizedName(),
                style = MaterialTheme.typography.displayMedium,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            ReleaseMetaText(release = release)
            release.description?.let { description ->
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }
            content?.invoke(this)
        }
    )
}

@Composable
private fun LargeReleaseCardLayout(
    modifier: Modifier = Modifier,
    contentModifier: Modifier = Modifier,
    poster: @Composable () -> Unit,
    content: @Composable ColumnScope.() -> Unit,
) {
    BoxWithConstraints {
        val height = calculateContainerHeight(maxWidth)
        val contentWidth = calculateContentWidth(maxWidth)

        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(height),
            contentAlignment = Alignment.BottomStart
        ) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .fadedEdge(edgeHeight = 350.dp)
            ) {
                poster()
            }

            Column(
                modifier = contentModifier
                    .width(contentWidth)
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                content = content
            )
        }
    }
}

@Composable
private fun LargeReleaseCardPlaceholder(
    modifier: Modifier = Modifier,
) {
    val shimmer = LocalShimmer.current

    LargeReleaseCardLayout(
        modifier = Modifier,
        poster = {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .shimmer(shimmer)
                    .background(Color.LightGray)
                    .fadedEdge(edgeHeight = 350.dp)
            )
        },
        content = {}
    )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Preview
@Composable
private fun LargeReleaseCardPreview() {
    val shimmer = rememberShimmer(ShimmerBounds.View)

    AnilibriaTheme {
        ProvideShimmer(shimmer) {
            Surface {
                LargeReleaseCard(
                    release = dummyReleaseList[2],
                ) {
                    MediumSplitButton(
                        onLeadingClick = {
                            // Handle leading button click
                        },
                        onTrailingClick = {
                            // Handle trailing button click
                        },
                        leadingContent = {
                            Icon(
                                modifier = Modifier.size(ButtonDefaults.MediumIconSize),
                                imageVector = AnilibriaIcons.Filled.PlayArrow,
                                contentDescription = null
                            )
                            Spacer(Modifier.width(ButtonDefaults.MediumIconSpacing))
                            Text(
                                text = stringResource(Res.string.button_watch),
                                maxLines = 1
                            )
                        },
                        trailingContent = {
                            Icon(
                                modifier = Modifier.size(SplitButtonDefaults.MediumTrailingButtonIconSize),
                                imageVector = AnilibriaIcons.Outlined.MoreVert,
                                contentDescription = null
                            )
                        }
                    )
                }
            }
        }
    }
}


@Composable
internal fun calculateContainerHeight(width: Dp): Dp {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val rawHeight = when {
        windowSizeClass.isWidthAtLeastBreakpoint(WIDTH_DP_MEDIUM_LOWER_BOUND)
                && windowSizeClass.isHeightAtLeastBreakpoint(HEIGHT_DP_MEDIUM_LOWER_BOUND) -> width * 4f / 7f

        windowSizeClass.isWidthAtLeastBreakpoint(WIDTH_DP_MEDIUM_LOWER_BOUND) -> width * 2.75f / 7f
        else -> width * 10f / 7f
    }

    return if (windowSizeClass.isWidthAtLeastBreakpoint(WIDTH_DP_MEDIUM_LOWER_BOUND)) {
        rawHeight.coerceAtMost(400.dp)
    } else {
        rawHeight
    }
}

@Composable
internal fun calculateContentWidth(width: Dp): Dp {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    return when {
        windowSizeClass.isWidthAtLeastBreakpoint(WIDTH_DP_MEDIUM_LOWER_BOUND) -> width * 0.6f
        else -> width
    }
}