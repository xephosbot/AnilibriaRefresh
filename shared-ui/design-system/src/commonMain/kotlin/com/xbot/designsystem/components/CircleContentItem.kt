package com.xbot.designsystem.components

import androidx.compose.animation.core.spring
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.xbot.designsystem.theme.ExpressiveShape
import com.xbot.designsystem.theme.ExpressiveTextStyle
import com.xbot.designsystem.theme.MorphingExpressiveShape
import com.xbot.designsystem.theme.MorphingExpressiveTextStyle
import com.xbot.domain.models.Genre
import com.xbot.domain.models.ReleaseMember
import com.xbot.domain.models.Poster
import com.xbot.localization.stringRes
import com.xbot.resources.Res
import com.xbot.resources.placeholder_profile
import com.xbot.resources.releases_count
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
        modifier = modifier.width(IntrinsicSize.Min),
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
                modifier = Modifier.matchParentSize(),
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
    private var _textStyle: ExpressiveTextStyle? = null

    @Composable
    fun shape(): ExpressiveShape {
        return _shape ?: MorphingExpressiveShape(
            shape = MaterialShapes.Circle,
            pressedShape = MaterialShapes.Cookie12Sided,
            animationSpec =  MaterialTheme.motionScheme.defaultSpatialSpec()
        ).also { _shape = it }
    }

    @Composable
    fun textStyle(): ExpressiveTextStyle {
        return _textStyle ?: MorphingExpressiveTextStyle(
            from = MaterialTheme.typography.labelMedium,
            to = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.ExtraBold),
            animationSpec = spring()
        ).also { _textStyle = it }
    }
}

private val PosterSize = 100.dp
private val SpaceHeight = 4.dp
private val SubtitleAlpha = 0.6f