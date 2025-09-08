package com.xbot.designsystem.components

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.shimmer
import com.xbot.designsystem.modifier.LocalShimmer
import com.xbot.designsystem.modifier.scrim
import com.xbot.designsystem.theme.ExpressiveShape
import com.xbot.designsystem.theme.RoundedCornerExpressiveShape
import com.xbot.designsystem.utils.PreviewContainer
import com.xbot.designsystem.utils.dummyReleaseList
import com.xbot.domain.models.Release
import com.xbot.localization.localizedName
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun SmallReleaseCard(
    release: Release?,
    modifier: Modifier = Modifier,
    shape: ExpressiveShape = ExpressiveReleaseCardItemDefaults.shape(),
    interactionSource: MutableInteractionSource? = null,
    onClick: (Release) -> Unit,
) {
    @Suppress("NAME_SHADOWING")
    val interactionSource = interactionSource ?: remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()

    Crossfade(
        modifier = Modifier
            .clip(shape.shapeForInteraction(pressed, false))
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
            null -> SmallReleaseCardPlaceholder(modifier)
            else -> SmallReleaseCardContent(modifier, state)
        }
    }
}

@Composable
private fun SmallReleaseCardContent(
    modifier: Modifier = Modifier,
    release: Release,
) {
    Box(
        modifier = modifier
            .width(ReleaseCardWidth)
            .aspectRatio(7f / 10f),
        contentAlignment = Alignment.BottomStart
    ) {
        PosterImage(
            modifier = Modifier
                .fillMaxSize()
                .scrim(edgeHeightRatio = 1.0f),
            poster = release.poster,
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            text = release.localizedName(),
            color = MaterialTheme.colorScheme.inverseOnSurface,
            style = MaterialTheme.typography.labelLarge,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun SmallReleaseCardPlaceholder(
    modifier: Modifier = Modifier,
) {
    val shimmer = LocalShimmer.current

    Box(
        modifier = modifier
            .width(ReleaseCardWidth)
            .aspectRatio(7f / 10f)
            .scrim(edgeHeightRatio = 1.0f)
            .shimmer(shimmer)
            .background(Color.LightGray),
    )
}

@Preview
@Composable
private fun SmallReleaseCardPreview() {
    PreviewContainer {
        SmallReleaseCard(
            release = dummyReleaseList[1],
            onClick = {
                // Handle the click event here
            }
        )
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
object ExpressiveReleaseCardItemDefaults {
    private var _shape: ExpressiveShape? = null

    @Composable
    fun shape(): ExpressiveShape {
        return _shape ?: RoundedCornerExpressiveShape(
            shape = RoundedCornerShape(16.dp),
            pressedShape = RoundedCornerShape(8.dp),
            selectedShape = RoundedCornerShape(8.dp),
            animationSpec = MaterialTheme.motionScheme.defaultSpatialSpec()
        ).also { _shape = it }
    }
}

private val ReleaseCardWidth = 132.dp
