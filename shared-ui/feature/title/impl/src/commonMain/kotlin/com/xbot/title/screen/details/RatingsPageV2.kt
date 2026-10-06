package com.xbot.title.screen.details

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.xbot.designsystem.components.ExpressiveEpisodeListItemDefaults
import com.xbot.designsystem.components.MemberItem
import com.xbot.designsystem.components.PosterImage
import com.xbot.designsystem.components.PreferenceItem
import com.xbot.designsystem.components.section
import com.xbot.designsystem.icons.AnilibertyIcons
import com.xbot.designsystem.icons.Insights
import com.xbot.designsystem.icons.MyAnimeListLogo
import com.xbot.designsystem.icons.ShikimoriLogo
import com.xbot.designsystem.shape.rememberMorphableShape
import com.xbot.domain.models.ExternalRating
import com.xbot.domain.models.Genre
import com.xbot.domain.models.ReleaseDetails
import com.xbot.domain.models.enums.CollectionType
import com.xbot.formatters.formatCompact
import com.xbot.formatters.formatDecimal
import com.xbot.formatters.stringRes
import com.xbot.formatters.toLocalizedString
import com.xbot.resources.Res
import com.xbot.resources.release_details_dropped_insight
import com.xbot.resources.release_details_external_votes
import com.xbot.resources.release_details_rating_votes
import com.xbot.resources.release_details_who_watches
import kotlin.time.Duration
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun RatingsPage(
    details: ReleaseDetails,
    isActive: Boolean,
    listState: LazyListState,
    headerSpacerHeight: Dp,
    bottomPadding: Dp,
    onAction: (TitleDetailsActionV2) -> Unit,
    modifier: Modifier = Modifier
) {
    var revealed by rememberSaveable { mutableStateOf(false) }
    LaunchedEffect(isActive) {
        if (isActive) revealed = true
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        state = listState,
        contentPadding = PaddingValues(bottom = bottomPadding)
    ) {
        item(key = HEADER_SPACER_KEY, contentType = HEADER_SPACER_KEY) {
            Spacer(Modifier.height(headerSpacerHeight))
        }
        if (details.rating != null || details.shikimoriRating != null ||
            details.myAnimeListRating != null
        ) {
            item(key = "rating") {
                RatingCard(
                    modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
                    details = details,
                    revealed = revealed,
                    onUrlClick = { url -> onAction(TitleDetailsActionV2.OnUrlClick(url)) }
                )
            }
        }
        if (details.collectionCounts.values.sum() > 0) {
            item(key = "community") {
                AboutSection(title = stringResource(Res.string.release_details_who_watches)) {
                    CommunityCard(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        collections = details.collectionCounts
                    )
                }
            }
        }
    }
}

@Composable
private fun RatingCard(
    details: ReleaseDetails,
    revealed: Boolean,
    onUrlClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.extraLarge,
        color = MaterialTheme.colorScheme.surfaceBright
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            details.rating?.let { rating ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    rating.average?.let { average ->
                        ScoreBadge(average = average, votes = rating.votes)
                    }
                    RatingHistogram(
                        modifier = Modifier.weight(1f),
                        votesByScore = rating.votesByScore,
                        revealed = revealed
                    )
                }
            }
            val external = listOfNotNull(
                details.shikimoriRating?.let {
                    Triple(AnilibertyIcons.ShikimoriLogo, SHIKIMORI_LABEL, it)
                },
                details.myAnimeListRating?.let {
                    Triple(AnilibertyIcons.MyAnimeListLogo, MY_ANIME_LIST_LABEL, it)
                }
            )
            if (external.isNotEmpty()) {
                Row(
                    modifier = Modifier.padding(top = if (details.rating != null) 14.dp else 0.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    external.forEach { (logo, name, rating) ->
                        ExternalRatingTile(
                            modifier = Modifier.weight(1f),
                            logo = logo,
                            name = name,
                            rating = rating,
                            onClick = { onUrlClick(rating.url) }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun ScoreBadge(average: Double, votes: Int, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(104.dp)
            .clip(MaterialShapes.Cookie9Sided.toShape())
            .background(MaterialTheme.colorScheme.primary),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = average.formatDecimal(appLocale()),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Text(
                text = pluralStringResource(
                    Res.plurals.release_details_rating_votes,
                    votes,
                    votes
                ),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Composable
private fun RatingHistogram(
    votesByScore: Map<Int, Int>,
    revealed: Boolean,
    modifier: Modifier = Modifier
) {
    val max = votesByScore.values.maxOrNull()?.takeIf { it > 0 } ?: 1
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        for (score in MAX_SCORE downTo 1) {
            val target = (votesByScore[score] ?: 0).toFloat() / max
            val fraction by animateFloatAsState(
                targetValue = if (revealed) target else 0f,
                animationSpec = tween(durationMillis = 800)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    modifier = Modifier.width(18.dp),
                    text = score.toString(),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(7.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceContainerHighest)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(fraction)
                            .fillMaxHeight()
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary)
                    )
                }
            }
        }
    }
}

@Composable
private fun ExternalRatingTile(
    logo: ImageVector,
    name: String,
    rating: ExternalRating,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val locale = appLocale()
    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.surfaceContainerHigh
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(
                modifier = Modifier.size(32.dp),
                imageVector = logo,
                contentDescription = null
            )
            Column {
                Text(
                    text = rating.rating.formatDecimal(locale),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = name,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = pluralStringResource(
                        Res.plurals.release_details_external_votes,
                        rating.votes,
                        rating.votes.formatCompact(locale)
                    ),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun CommunityCard(collections: Map<CollectionType, Int>, modifier: Modifier = Modifier) {
    val locale = appLocale()
    val colors = collectionColors()
    val total = collections.values.sum()
    val abandoned = collections[CollectionType.ABANDONED] ?: 0
    val abandonedShare = abandoned.toDouble() / total

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.extraLarge,
        color = MaterialTheme.colorScheme.surfaceBright
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp),
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                CollectionStatusOrder.forEach { status ->
                    val count = collections[status] ?: 0
                    if (count > 0) {
                        Box(
                            modifier = Modifier
                                .weight(count.toFloat())
                                .fillMaxHeight()
                                .clip(CircleShape)
                                .background(colors.getValue(status))
                        )
                    }
                }
            }
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                maxItemsInEachRow = 2,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                CollectionStatusOrder.forEach { status ->
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .background(colors.getValue(status), CircleShape)
                        )
                        Text(
                            modifier = Modifier.weight(1f),
                            text = stringResource(status.labelRes),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = (collections[status] ?: 0).formatGrouped(),
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
            }
            if (abandonedShare < DROPPED_INSIGHT_THRESHOLD) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = AnilibertyIcons.Insights,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = stringResource(
                            Res.string.release_details_dropped_insight,
                            "${(abandonedShare * 100).formatDecimal(locale, digits = 1)}%"
                        ),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

@Composable
private fun collectionColors(): Map<CollectionType, Color> {
    val scheme = MaterialTheme.colorScheme
    return mapOf(
        CollectionType.WATCHING to scheme.primary,
        CollectionType.PLANNED to scheme.tertiary,
        CollectionType.WATCHED to scheme.secondary,
        CollectionType.POSTPONED to scheme.inversePrimary,
        CollectionType.ABANDONED to scheme.outlineVariant
    )
}

private const val MAX_SCORE = 10
private const val DROPPED_INSIGHT_THRESHOLD = 0.05
private const val SHIKIMORI_LABEL = "Shikimori"
private const val MY_ANIME_LIST_LABEL = "MyAnimeList"
