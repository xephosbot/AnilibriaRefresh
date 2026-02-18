package com.xbot.designsystem.components

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.shimmer
import com.xbot.designsystem.modifier.LocalShimmer
import com.xbot.designsystem.theme.ExpressiveShape
import com.xbot.designsystem.theme.MorphingExpressiveShape
import com.xbot.designsystem.utils.AnilibriaPreview
import com.xbot.domain.models.Genre
import com.xbot.domain.models.Poster
import com.xbot.domain.models.ReleaseMember
import com.xbot.domain.models.enums.MemberRole
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
                image = {
                    PosterImage(
                        modifier = Modifier.fillMaxSize(),
                        poster = state.image,
                    )
                },
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
            CircleContentPlaceholder()
        }
    }
}

@Composable
fun MemberItem(
    releaseMember: ReleaseMember?,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Crossfade(
        targetState = releaseMember,
        modifier = modifier,
        label = "MemberItem Crossfade"
    ) { state ->
        if (state != null) {
            CircleContentItem(
                modifier = modifier,
                onClick = onClick,
                image = {
                    PosterImage(
                        modifier = Modifier.fillMaxSize(),
                        poster = state.avatar,
                        placeholder = painterResource(Res.drawable.placeholder_profile),
                    )
                },
                title = {
                    Text(
                        text = state.name,
                        textAlign = TextAlign.Center
                    )
                },
                subtitle = {
                    state.role?.let { role ->
                        Text(
                            text = stringResource(role.stringRes),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            )
        } else {
            CircleContentPlaceholder()
        }
    }
}

@Composable
private fun CircleContentPlaceholder(
    modifier: Modifier = Modifier
) {
    val shimmer = LocalShimmer.current
    CircleContentItem(
        onClick = {},
        modifier = modifier,
        image = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .shimmer(shimmer)
                    .background(Color.LightGray)
            )
        },
        title = {
            Box(
                modifier = Modifier
                    .width(80.dp)
                    .height(16.dp)
                    .clip(MaterialTheme.shapes.small)
                    .shimmer(shimmer)
                    .background(Color.LightGray)
            )
        },
        subtitle = {
            Box(
                modifier = Modifier
                    .width(40.dp)
                    .height(12.dp)
                    .clip(MaterialTheme.shapes.small)
                    .shimmer(shimmer)
                    .background(Color.LightGray)
            )
        }
    )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun CircleContentItem(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    shape: ExpressiveShape = CircleContentItemDefaults.shape(),
    image: @Composable BoxScope.() -> Unit,
    title: @Composable () -> Unit,
    subtitle: @Composable () -> Unit,
    interactionSource: MutableInteractionSource? = null,
) {
    @Suppress("NAME_SHADOWING")
    val interactionSource = interactionSource ?: remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val focused by interactionSource.collectIsFocusedAsState()
    val hovered by interactionSource.collectIsHoveredAsState()
    val dragged by interactionSource.collectIsDraggedAsState()

    val shape = shape.shapeForInteraction(
        pressed = pressed,
        selected = false,
        focused = focused,
        hovered = hovered,
        dragged = dragged
    )

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(PosterSize)
                .clip(shape)
                .clickable(
                    interactionSource = interactionSource,
                    indication = LocalIndication.current,
                    onClick = onClick
                )
        ) {
            image()
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

@Preview
@Composable
private fun GenreItemPreview() {
    AnilibriaPreview {
        GenreItem(
            genre = Genre(
                id = 0,
                name = "Комедия",
                releasesCount = 15,
                image = Poster(
                    src = "https://anilibria.tv/upload/release/genres/comedy.jpg",
                    thumbnail = null
                )
            ),
            onClick = {}
        )
    }
}

@Preview
@Composable
private fun MemberItemPreview() {
    AnilibriaPreview {
        MemberItem(
            releaseMember = ReleaseMember(
                id = "0",
                name = "Lupin",
                role = MemberRole.VOICING,
                avatar = Poster(
                    src = "https://anilibria.tv/upload/avatars/0.jpg",
                    thumbnail = null
                )
            ),
            onClick = {}
        )
    }
}

@Preview
@Composable
private fun CircleContentPlaceholderPreview() {
    AnilibriaPreview {
        CircleContentPlaceholder()
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
object CircleContentItemDefaults {
    @Composable
    fun shape(): ExpressiveShape {
        return MorphingExpressiveShape(
            shape = MaterialShapes.Circle,
            pressedShape = MaterialShapes.Cookie7Sided,
            animationSpec = MaterialTheme.motionScheme.fastSpatialSpec()
        )
    }
}

private val PosterSize = 100.dp
private val SpaceHeight = 4.dp
private val SubtitleAlpha = 0.6f
