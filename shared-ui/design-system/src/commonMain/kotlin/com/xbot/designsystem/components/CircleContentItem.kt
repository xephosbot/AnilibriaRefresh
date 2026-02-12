package com.xbot.designsystem.components

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.shimmer
import com.xbot.designsystem.modifier.LocalShimmer
import com.xbot.designsystem.theme.ExpressiveShape
import com.xbot.designsystem.theme.MorphingExpressiveShape
import com.xbot.domain.models.Genre
import com.xbot.domain.models.Poster
import com.xbot.domain.models.ReleaseMember
import com.xbot.localization.stringRes
import com.xbot.resources.Res
import com.xbot.resources.placeholder_profile
import com.xbot.resources.releases_count
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun GenreItem(
    genre: Genre?,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Crossfade(
        targetState = genre,
        modifier = modifier,
        label = "GenreItem Crossfade"
    ) { state ->
        if (state != null) {
            CircleContentItem(
                modifier = Modifier,
                onClick = onClick,
                poster = state.image,
                title = {
                    Text(
                        text = state.name,
                        textAlign = TextAlign.Center
                    )
                },
                subtitle = {
                    state.releasesCount?.let { count ->
                        Text(
                            text = stringResource(Res.string.releases_count, count),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            )
        } else {
            GenreItemPlaceholder()
        }
    }
}

@Composable
private fun GenreItemPlaceholder() {
    val shimmer = LocalShimmer.current
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(PosterSize)
                .clip(CircleShape)
                .shimmer(shimmer)
                .background(Color.LightGray)
        )
        Spacer(Modifier.height(SpaceHeight))
        Box(
            modifier = Modifier
                .width(80.dp)
                .height(16.dp)
                .clip(MaterialTheme.shapes.small)
                .shimmer(shimmer)
                .background(Color.LightGray)
        )
        Spacer(Modifier.height(2.dp))
        Box(
            modifier = Modifier
                .width(40.dp)
                .height(12.dp)
                .clip(MaterialTheme.shapes.small)
                .shimmer(shimmer)
                .background(Color.LightGray)
        )
    }
}

@Composable
fun MemberItem(
    releaseMember: ReleaseMember,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    CircleContentItem(
        modifier = modifier,
        onClick = onClick,
        poster = releaseMember.avatar,
        placeholder = painterResource(Res.drawable.placeholder_profile),
        title = {
            Text(
                text = releaseMember.name,
                textAlign = TextAlign.Center
            )
        },
        subtitle = {
            releaseMember.role?.let { role ->
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
    shape: ExpressiveShape = CircleContentItemDefaults.shape(),
    title: @Composable () -> Unit,
    subtitle: @Composable () -> Unit,
    interactionSource: MutableInteractionSource? = null,
) {
    @Suppress("NAME_SHADOWING")
    val interactionSource = interactionSource ?: remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(PosterSize)
                .clip(shape.shapeForInteraction(pressed, false))
                .clickable(
                    interactionSource = interactionSource,
                    indication = LocalIndication.current,
                    onClick = onClick
                )
        ) {
            PosterImage(
                modifier = Modifier.fillMaxSize(),
                poster = poster,
                placeholder = placeholder,
            )
        }
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

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
object CircleContentItemDefaults {
    private var _shape: ExpressiveShape? = null

    @Composable
    fun shape(): ExpressiveShape {
        return _shape ?: MorphingExpressiveShape(
            shape = MaterialShapes.Circle,
            pressedShape = MaterialShapes.Cookie7Sided,
            animationSpec =  MaterialTheme.motionScheme.defaultSpatialSpec()
        ).also { _shape = it }
    }
}

private val PosterSize = 100.dp
private val SpaceHeight = 4.dp
private val SubtitleAlpha = 0.6f