package com.xbot.designsystem.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.xbot.designsystem.theme.AnilibriaTheme
import com.xbot.designsystem.utils.dummyReleaseList
import com.xbot.domain.models.Release
import com.xbot.domain.models.enums.AgeRating
import com.xbot.domain.models.enums.ReleaseType
import com.xbot.localization.stringRes
import com.xbot.resources.Res
import com.xbot.resources.episode_abbreviation
import com.xbot.resources.minutes_abbreviation
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ReleaseMetaText(
    release: Release,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodyMedium
) {
    val inlineAgeRatingPill = InlineTextContent(
        placeholder = Placeholder(
            width = 28.dpTextUnit,
            height = 18.dpTextUnit,
            placeholderVerticalAlign = PlaceholderVerticalAlign.TextCenter
        )
    ) {
        ReleaseAgeRatingPill(
            modifier = Modifier.fillMaxSize(),
            ageRating = release.ageRating
        )
    }

    val annotatedString = buildAnnotatedString {
        appendInlineContent(AGE_RATING_PILL_TAG)
        append(" \u2022 ")
        append(release.year.toString())
        append(" \u2022 ")
        release.type?.let { type ->
            when (type) {
                ReleaseType.MOVIE -> {
                    append(stringResource(type.stringRes))
                    append(" \u2022 ")
                }
                else -> release.episodesCount?.let { episodesCount ->
                    append(stringResource(Res.string.episode_abbreviation, episodesCount.toString()))
                    append(" \u2022 ")
                }
            }
        }
        release.episodeDuration?.let { episodeDuration ->
            append(stringResource(Res.string.minutes_abbreviation, episodeDuration.toString()))
            append(" \u2022 ")
        }
        append("${release.favoritesCount} \u2605")
    }

    Text(
        modifier = modifier,
        text = annotatedString,
        inlineContent = mapOf(AGE_RATING_PILL_TAG to inlineAgeRatingPill),
        style = style,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
    )
}

@Composable
private fun ReleaseAgeRatingPill(
    ageRating: AgeRating,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.inverseSurface,
    contentColor: Color = contentColorFor(color)
) {
    Surface(
        color = color,
        contentColor = contentColor,
        shape = RoundedCornerShape(4.dp)
    ) {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(ageRating.stringRes),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Preview
@Composable
private fun ReleaseMetaTextPreview() {
    AnilibriaTheme {
        Surface {
            ReleaseMetaText(release = dummyReleaseList[1])
        }
    }
}

@Preview
@Composable
private fun ReleaseMetaTextPreviewDark() {
    AnilibriaTheme(darkTheme = true) {
        Surface {
            ReleaseMetaText(release = dummyReleaseList[1])
        }
    }
}

private val Int.dpTextUnit: TextUnit
    @Composable
    get() = with(LocalDensity.current) { this@dpTextUnit.dp.toSp() }

private const val AGE_RATING_PILL_TAG = "age_rating_pill"