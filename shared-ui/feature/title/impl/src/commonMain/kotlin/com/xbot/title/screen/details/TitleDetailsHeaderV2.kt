package com.xbot.title.screen.details

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledIconToggleButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilledTonalToggleButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.xbot.designsystem.components.ConnectedButtonGroupDefaults
import com.xbot.designsystem.components.PosterImage
import com.xbot.designsystem.components.icon
import com.xbot.designsystem.icons.AnilibertyIcons
import com.xbot.designsystem.icons.Check
import com.xbot.designsystem.icons.Copyright
import com.xbot.designsystem.icons.OpenInNew
import com.xbot.designsystem.icons.PlaylistPlay
import com.xbot.designsystem.icons.PublicOff
import com.xbot.domain.models.Poster
import com.xbot.domain.models.ReleaseDetails
import com.xbot.domain.models.enums.CollectionType
import com.xbot.domain.models.isFinished
import com.xbot.formatters.formatOrdinal
import com.xbot.formatters.stringRes
import com.xbot.resources.Res
import com.xbot.resources.release_details_episode
import com.xbot.resources.release_details_episodes_meta
import com.xbot.resources.release_details_finished
import com.xbot.resources.release_details_open_external_player
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun ReleaseHero(poster: Poster?, modifier: Modifier = Modifier) {
    val background = MaterialTheme.colorScheme.surfaceContainer
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(HeroHeight)
    ) {
        PosterImage(
            modifier = Modifier.fillMaxSize(),
            poster = poster
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        0.35f to Color.Transparent,
                        0.72f to background.copy(alpha = 0.7f),
                        1f to background
                    )
                )
        )
    }
}

@Composable
internal fun ReleaseMetaChips(details: ReleaseDetails, modifier: Modifier = Modifier) {
    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        val release = details.release
        if (release.isFinished) {
            MetaChip(
                text = stringResource(Res.string.release_details_finished),
                icon = AnilibertyIcons.Check
            )
        }
        MetaChip(
            text = stringResource(release.ageRating.stringRes),
            containerColor = MaterialTheme.colorScheme.inverseSurface,
            fontWeight = FontWeight.Bold
        )
        release.type?.let { type ->
            MetaChip(text = stringResource(type.stringRes), icon = type.icon)
        }
        release.season?.let { season ->
            MetaChip(
                text = "${stringResource(season.stringRes)} ${release.year}",
                icon = season.icon
            )
        }
        val episodesCount = release.episodesCount
        val episodeDuration = release.episodeDuration
        if (episodesCount != null && episodeDuration != null) {
            MetaChip(
                text = stringResource(
                    Res.string.release_details_episodes_meta,
                    episodesCount,
                    episodeDuration
                ),
                icon = AnilibertyIcons.PlaylistPlay
            )
        }
    }
}

@Composable
private fun MetaChip(
    text: String,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainerHigh,
    fontWeight: FontWeight = FontWeight.Medium,
    onClick: (() -> Unit)? = null
) {
    val content: @Composable () -> Unit = {
        Row(
            modifier = Modifier
                .height(MetaChipHeight)
                .padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            icon?.let {
                Icon(
                    modifier = Modifier.size(16.dp),
                    imageVector = it,
                    contentDescription = null
                )
            }
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = fontWeight
            )
        }
    }
    val shape = MaterialTheme.shapes.small
    if (onClick != null) {
        Surface(
            onClick = onClick,
            modifier = modifier,
            shape = shape,
            color = containerColor,
            content = content
        )
    } else {
        Surface(
            modifier = modifier,
            shape = shape,
            color = containerColor,
            content = content
        )
    }
}

@Composable
internal fun ReleaseTitleBlock(details: ReleaseDetails, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(
            text = details.release.name,
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.ExtraBold
        )
        details.release.englishName?.let { englishTitle ->
            Text(
                modifier = Modifier.padding(top = 8.dp),
                text = englishTitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        details.alternativeName?.let { alternativeName ->
            Text(
                modifier = Modifier.padding(top = 2.dp),
                text = alternativeName,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
internal fun episodeLabel(ordinal: Float): String =
    stringResource(Res.string.release_details_episode, ordinal.formatOrdinal())

internal val HeroHeight = 440.dp
private val MetaChipHeight = 28.dp
