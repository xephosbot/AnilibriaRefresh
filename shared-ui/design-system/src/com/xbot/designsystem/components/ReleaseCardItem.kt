package com.xbot.designsystem.components

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.spring
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
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
import com.xbot.designsystem.modifier.LocalShimmer
import com.xbot.designsystem.modifier.scrim
import com.xbot.designsystem.modifier.shimmerSafe
import com.xbot.designsystem.theme.ExpressiveShape
import com.xbot.designsystem.theme.RoundedCornerExpressiveShape
import com.xbot.domain.models.Release
import com.xbot.localization.localizedName

@Composable
fun ReleaseCardItem(
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
            null -> LoadingReleaseCardItem(modifier)
            else -> ReleaseCardItemContent(modifier, state)
        }
    }
}

@Composable
private fun ReleaseCardItemContent(
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
                .scrim(1.0f),
            poster = release.poster,
        )
        Text(
            modifier = Modifier.padding(12.dp),
            text = release.localizedName(),
            color = Color.White,
            style = MaterialTheme.typography.titleSmall,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun LoadingReleaseCardItem(
    modifier: Modifier = Modifier,
) {
    val shimmer = LocalShimmer.current

    Box(
        modifier = modifier
            .width(ReleaseCardWidth)
            .aspectRatio(7f / 10f)
            .scrim(1.0f)
            .shimmerSafe(shimmer)
            .background(Color.LightGray),
    )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
object ExpressiveReleaseCardItemDefaults {
    private var _shape: ExpressiveShape? = null

    @Composable
    fun shape(): ExpressiveShape {
        return _shape ?: RoundedCornerExpressiveShape(
            shape = RoundedCornerShape(24.dp),
            pressedShape = RoundedCornerShape(8.dp),
            selectedShape = RoundedCornerShape(8.dp),
            animationSpec = spring()
        ).also { _shape = it }
    }
}

private val ReleaseCardWidth = 124.dp
