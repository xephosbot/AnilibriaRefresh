package com.xbot.designsystem.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import com.xbot.designsystem.icons.AnilibertyIcons
import com.xbot.designsystem.icons.VideoLibrary
import com.xbot.designsystem.utils.AnilibertyPreview
import com.xbot.domain.fixtures.ReleaseFixtures
import com.xbot.domain.models.Release
import com.xbot.domain.models.enums.ReleaseType
import com.xbot.domain.models.isFinished
import com.xbot.formatters.stringRes
import com.xbot.formatters.toLocalizedString
import com.xbot.resources.Res
import com.xbot.resources.episode_abbreviation
import com.xbot.resources.minutes_abbreviation
import com.xbot.resources.release_details_episodes_meta
import com.xbot.resources.release_details_finished
import kotlin.time.Duration.Companion.minutes
import org.jetbrains.compose.resources.stringResource

@Composable
fun ReleaseMetaText(
    release: Release,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodyMedium,
    maxLines: Int = 1
) {
    val parts = releaseMetaParts(release)
    val ageRatingPill = metaPill(
        text = stringResource(release.ageRating.stringRes),
        color = MaterialTheme.colorScheme.inverseSurface
    )
    val finishedPill = if (release.isFinished) {
        metaPill(
            text = stringResource(Res.string.release_details_finished),
            color = MaterialTheme.colorScheme.secondaryContainer
        )
    } else {
        null
    }
    val inlineContent = buildMap {
        put(AGE_RATING_PILL_TAG, ageRatingPill)
        finishedPill?.let { put(FINISHED_PILL_TAG, it) }
        parts.forEach { part ->
            part.icon?.let { icon -> put(part.iconTag, iconContent(icon)) }
        }
    }
    val text = buildAnnotatedString {
        appendInlineContent(AGE_RATING_PILL_TAG)
        if (finishedPill != null) {
            append(NO_BREAK_SPACE)
            appendInlineContent(FINISHED_PILL_TAG)
        }
        parts.forEach { part ->
            append(SEPARATOR)
            if (part.icon != null) {
                appendInlineContent(part.iconTag)
                append(NO_BREAK_SPACE)
            }
            append(part.text)
        }
    }

    Text(
        modifier = modifier,
        text = text,
        inlineContent = inlineContent,
        style = style,
        maxLines = maxLines,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
private fun releaseMetaParts(release: Release): List<MetaPart> {
    val season = release.season
    val yearAndSeason = MetaPart(
        text = season?.let { "${stringResource(it.stringRes)} ${release.year}" }
            ?: release.year.toString(),
        icon = season?.icon,
        iconTag = SEASON_ICON_TAG
    )
    val duration = if (release.type == ReleaseType.MOVIE) {
        val movie = stringResource(ReleaseType.MOVIE.stringRes)
        val duration = release.episodeDuration?.minutes?.toLocalizedString()
        MetaPart(
            text = listOfNotNull(movie, duration).joinToString(", "),
            icon = ReleaseType.MOVIE.icon,
            iconTag = MOVIE_ICON_TAG
        )
    } else {
        val episodesCount = release.episodesCount
        val episodeDuration = release.episodeDuration
        val text = when {
            episodesCount != null && episodeDuration != null ->
                stringResource(
                    Res.string.release_details_episodes_meta,
                    episodesCount,
                    episodeDuration
                )

            episodesCount != null ->
                stringResource(Res.string.episode_abbreviation, episodesCount.toString())

            episodeDuration != null ->
                stringResource(Res.string.minutes_abbreviation, episodeDuration.toString())

            else -> null
        }
        text?.let {
            MetaPart(text = it, icon = AnilibertyIcons.VideoLibrary, iconTag = EPISODES_ICON_TAG)
        }
    }
    return listOfNotNull(yearAndSeason, duration)
}

private data class MetaPart(
    val text: String,
    val icon: ImageVector? = null,
    val iconTag: String = ""
)

@Composable
private fun metaPill(text: String, color: Color): InlineTextContent {
    val style = MaterialTheme.typography.bodySmall
    val textMeasurer = rememberTextMeasurer()
    val density = LocalDensity.current
    val textWidth = remember(text, style, density) { textMeasurer.measure(text, style).size.width }
    val width = with(density) { (textWidth.toDp() + PillHorizontalPadding * 2).toSp() }
    val height = with(density) { PillHeight.toSp() }
    return InlineTextContent(
        placeholder = Placeholder(
            width = width,
            height = height,
            placeholderVerticalAlign = PlaceholderVerticalAlign.TextCenter
        )
    ) {
        MetaPill(
            modifier = Modifier.fillMaxSize(),
            text = text,
            color = color
        )
    }
}

private fun iconContent(icon: ImageVector) = InlineTextContent(
    placeholder = Placeholder(
        width = ICON_SIZE_EM.em,
        height = ICON_SIZE_EM.em,
        placeholderVerticalAlign = PlaceholderVerticalAlign.TextCenter
    )
) {
    Icon(
        modifier = Modifier.fillMaxSize(),
        imageVector = icon,
        contentDescription = null
    )
}

@Composable
private fun MetaPill(
    text: String,
    color: Color,
    modifier: Modifier = Modifier,
    contentColor: Color = contentColorFor(color)
) {
    Surface(
        modifier = modifier,
        color = color,
        contentColor = contentColor,
        shape = MaterialTheme.shapes.extraSmall
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = text,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1
            )
        }
    }
}

@AnilibertyPreview
@Composable
private fun ReleaseMetaTextPreview() {
    ReleaseMetaText(release = ReleaseFixtures.all[1])
}

private const val AGE_RATING_PILL_TAG = "age_rating_pill"
private const val FINISHED_PILL_TAG = "finished_pill"
private const val MOVIE_ICON_TAG = "movie_icon"
private const val SEASON_ICON_TAG = "season_icon"
private const val EPISODES_ICON_TAG = "episodes_icon"
private const val SEPARATOR = " • "
private const val NO_BREAK_SPACE = " "
private const val ICON_SIZE_EM = 1.2f
private val PillHorizontalPadding = 6.dp
private val PillHeight = 18.dp
