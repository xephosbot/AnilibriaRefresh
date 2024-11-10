package com.xbot.anilibriarefresh.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.xbot.anilibriarefresh.models.Title
import com.xbot.anilibriarefresh.models.TitleTag
import com.xbot.anilibriarefresh.ui.utils.LocalShimmer
import com.xbot.anilibriarefresh.ui.utils.shimmerSafe
import com.xbot.anilibriarefresh.ui.utils.stringResource

@Composable
fun TitlePagerItem(
    modifier: Modifier = Modifier,
    title: Title,
    onClick: (Title) -> Unit,
) {
    val isCompact = LocalConfiguration.current.screenWidthDp.dp < 600.dp

    if (isCompact) {
        CompactItemLayout(
            modifier = modifier,
            title = title,
            onClick = onClick
        )
    } else {
        LargeItemLayout(
            modifier = modifier,
            title = title,
            onClick = onClick
        )
    }
}

@Composable
fun LoadingPagerItem(
    modifier: Modifier = Modifier,
) {
    val shimmer = LocalShimmer.current
    val isCompact = LocalConfiguration.current.screenWidthDp.dp < 600.dp

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = HorizontalPadding)
            .aspectRatio(if (isCompact) 7f / 10f else 1f)
            .height(if (isCompact) Dp.Unspecified else LargeItemHeight)
            .shimmerSafe(shimmer)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.LightGray)
    )
}

@Composable
private fun CompactItemLayout(
    modifier: Modifier,
    title: Title,
    onClick: (Title) -> Unit,
) {
    Box(
        modifier = Modifier
            .padding(horizontal = HorizontalPadding)
            .then(modifier)
            .clip(PagerItemShape),
        contentAlignment = Alignment.Center
    ) {
        PosterImage(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(7f / 10f)
                .clickable { onClick(title) },
            poster = title.poster
        )
    }
}

@Composable
private fun LargeItemLayout(
    modifier: Modifier,
    title: Title,
    onClick: (Title) -> Unit,
) {
    Box(
        modifier = Modifier
            .padding(horizontal = HorizontalPadding)
            .then(modifier)
            .clickable { onClick(title) }
            .height(LargeItemHeight)
            .clip(PagerItemShape)
            //TODO: Temporary background until final design
            .background(MaterialTheme.colorScheme.tertiaryContainer),
        contentAlignment = Alignment.Center
    ) {
        Row {
            PosterImage(
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(7f / 10f)
                    .clip(RoundedCornerShape(16.dp)),
                poster = title.poster
            )
            Column(
                modifier = Modifier
                    .padding(HorizontalPadding)
                    .weight(1f),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = title.name,
                    fontSize = 24.sp,
                    overflow = TextOverflow.Ellipsis
                )
                Row {
                    title.tags.forEachIndexed { index, tag ->
                        when (tag) {
                            is TitleTag.Text -> Text(
                                text = stringResource(tag.text),
                                fontSize = 14.sp
                            )

                            is TitleTag.TextWithIcon -> TextWithIcon(
                                text = stringResource(tag.text),
                                imageVector = tag.icon,
                                iconPosition = IconPosition.END,
                                fontSize = 14.sp
                            )
                        }
                        if (index < title.tags.lastIndex) {
                            Text(
                                text = "•",
                                fontSize = 14.sp
                            )
                        }
                    }
                }
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = title.description,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

fun Modifier.pagerItemTransition(
    page: Int,
    state: PagerState,
) = zIndex(page * 10f)
    .graphicsLayer {
        val startOffset = state.startOffsetForPage(page)
        translationX = size.width * (startOffset * .99f)
        alpha = (2f - startOffset) / 2f

        val scale = 1f - (startOffset * .1f)
        scaleX = scale
        scaleY = scale
    }

private fun PagerState.offsetForPage(page: Int) = (currentPage - page) + currentPageOffsetFraction

private fun PagerState.startOffsetForPage(page: Int): Float {
    return offsetForPage(page).coerceAtLeast(0f)
}

private val HorizontalPadding = 32.dp
private val LargeItemHeight = 300.dp
private val PagerItemShape = RoundedCornerShape(16.dp)