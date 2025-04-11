@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.xbot.shared.ui.designsystem.components

import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.graphics.shapes.Morph
import androidx.graphics.shapes.RoundedPolygon
import com.xbot.shared.ui.designsystem.utils.MorphPolygonShape
import com.xbot.shared.domain.models.Genre
import com.xbot.shared.domain.models.Member
import com.xbot.shared.domain.models.Poster
import com.xbot.shared.resources.Res
import com.xbot.shared.resources.placeholder_profile
import com.xbot.shared.resources.releases_count
import com.xbot.shared.ui.localization.stringRes
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun GenreItem(
    genre: Genre,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    CircleContentItem(
        modifier = modifier,
        onClick = onClick,
        poster = genre.image,
        title = {
            Text(
                text = genre.name,
                textAlign = TextAlign.Center
            )
        },
        subtitle = {
            genre.releasesCount?.let { count ->
                Text(
                    text = stringResource(Res.string.releases_count, count),
                    textAlign = TextAlign.Center
                )
            }
        }
    )
}

@Composable
fun MemberItem(
    member: Member,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    CircleContentItem(
        modifier = modifier,
        onClick = onClick,
        poster = member.avatar,
        placeholder = painterResource(Res.drawable.placeholder_profile),
        title = {
            Text(
                text = member.name,
                textAlign = TextAlign.Center
            )
        },
        subtitle = {
            member.role?.let { role ->
                Text(
                    text = stringResource(role.stringRes),
                    textAlign = TextAlign.Center
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun CircleContentItem(
    onClick: () -> Unit,
    poster: Poster?,
    modifier: Modifier = Modifier,
    placeholder: Painter? = null,
    shapes: CircleItemShapes = CircleContentItemDefaults.shapes(),
    title: @Composable () -> Unit,
    subtitle: @Composable () -> Unit,
    interactionSource: MutableInteractionSource? = null,
) {
    @Suppress("NAME_SHADOWING")
    val interactionSource = interactionSource ?: remember { MutableInteractionSource() }
    val defaultAnimationSpec = MaterialTheme.motionScheme.defaultSpatialSpec<Float>()
    val pressed by interactionSource.collectIsPressedAsState()
    val shape = shapeByInteraction(shapes, pressed, defaultAnimationSpec)

    Column(
        modifier = modifier.width(IntrinsicSize.Min),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PosterImage(
            modifier = Modifier
                .size(PosterSize)
                .clip(shape)
                .clickable(
                    interactionSource = interactionSource,
                    indication = LocalIndication.current,
                    onClick = onClick
                ),
            poster = poster,
            placeholder = placeholder,
        )
        Spacer(Modifier.height(SpaceHeight))
        ProvideTextStyle(MaterialTheme.typography.labelMedium) {
            title()
        }
        ProvideTextStyle(MaterialTheme.typography.bodySmall) {
            Box(modifier = Modifier.graphicsLayer { alpha = SubtitleAlpha }) {
                subtitle()
            }
        }
    }
}

object CircleContentItemDefaults {
    fun shapes(): CircleItemShapes {
        return CircleItemShapes(
            shape = MaterialShapes.Circle,
            pressedShape = MaterialShapes.Cookie12Sided
        )
    }
}

@Immutable
data class CircleItemShapes(
    val shape: RoundedPolygon,
    val pressedShape: RoundedPolygon
)

@Composable
private fun shapeByInteraction(
    shapes: CircleItemShapes,
    pressed: Boolean,
    animationSpec: FiniteAnimationSpec<Float>
): Shape {
    val morph = remember {
        Morph(shapes.shape, shapes.pressedShape)
    }
    val animatedProgress = animateFloatAsState(
        targetValue = if (pressed) 1f else 0f,
        label = "progress",
        animationSpec = animationSpec
    )

    return MorphPolygonShape(morph, animatedProgress.value)
}

private val PosterSize = 100.dp
private val SpaceHeight = 4.dp
private val SubtitleAlpha = 0.6f