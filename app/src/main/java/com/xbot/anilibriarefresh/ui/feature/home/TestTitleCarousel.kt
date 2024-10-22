package com.xbot.anilibriarefresh.ui.feature.home

import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.xbot.anilibriarefresh.ui.components.HorizontalPagerIndicator
import com.xbot.anilibriarefresh.ui.components.PosterImage
import com.xbot.domain.model.TitleModel

fun LazyGridScope.testTitleCarousel(
    items: List<TitleModel>,
    state: PagerState
) {
    item(
        span = { GridItemSpan(maxLineSpan) },
        contentType = { "PagerItems" }
    ) {
        BoxWithConstraints {
            if (maxWidth < 600.dp) {
                CompactLayout(
                    items = items,
                    state = state
                )
            } else {
                LargeLayout(
                    items, state
                )
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun CompactLayout(
    items: List<TitleModel>,
    state: PagerState,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalPager(
            state = state,
            beyondViewportPageCount = 3
        ) { page ->
            Box(
                modifier = Modifier
                    .zIndex(page * 10f)
                    .padding(
                        start = 32.dp,
                        end = 32.dp,
                    )
                    .graphicsLayer {
                        val startOffset = state.startOffsetForPage(page)
                        translationX = size.width * (startOffset * .99f)

                        alpha = (2f - startOffset) / 2f
                        val blur = (startOffset * 20f).coerceAtLeast(0.1f)
                        renderEffect = RenderEffect
                            .createBlurEffect(
                                blur, blur, Shader.TileMode.DECAL
                            )
                            .asComposeRenderEffect()

                        val scale = 1f - (startOffset * .1f)
                        scaleX = scale
                        scaleY = scale
                    }
                    .clip(RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                PosterImage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(7f / 10f)
                        .clickable { },
                    poster = items[page].poster
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        val verticalState = rememberPagerState(pageCount = { state.pageCount })

        VerticalPager(
            modifier = Modifier
                .height(64.dp)
                .padding(horizontal = 32.dp),
            state = verticalState,
            userScrollEnabled = false
        ) { page ->
            Column(
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = items[page].name,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Thin,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Row(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(bottom = 12.dp)
                ) {
                    items[page].tags.forEachIndexed { index, tag ->
                        Text(
                            text = tag,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1,
                        )
                        if (index != items[page].tags.lastIndex) Text(" • ")
                    }
                }
            }
        }

        LaunchedEffect(Unit) {
            snapshotFlow {
                Pair(
                    state.currentPage,
                    state.currentPageOffsetFraction
                )
            }.collect { (page, offset) ->
                verticalState.scrollToPage(page, offset)
            }
        }

        HorizontalPagerIndicator(
            modifier = Modifier.padding(top = 16.dp),
            state = state
        )
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun LargeLayout(
    items: List<TitleModel>,
    state: PagerState,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        HorizontalPager(
            state = state,
            beyondViewportPageCount = 3
        ) { page ->
            Box(
                modifier = Modifier
                    .zIndex(page * 10f)
                    .padding(
                        start = 32.dp,
                        end = 32.dp,
                    )
                    .graphicsLayer {
                        val startOffset = state.startOffsetForPage(page)
                        translationX = size.width * (startOffset * .99f)

                        alpha = (2f - startOffset) / 2f
                        val blur = (startOffset * 20f).coerceAtLeast(0.1f)
                        renderEffect = RenderEffect
                            .createBlurEffect(
                                blur, blur, Shader.TileMode.DECAL
                            )
                            .asComposeRenderEffect()

                        val scale = 1f - (startOffset * .1f)
                        scaleX = scale
                        scaleY = scale
                    }
                    .clip(RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Box(modifier = Modifier.clickable {  }) {
                    PosterImage(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .blur(15.dp)
                            .graphicsLayer {
                                alpha = 0.5f
                            },
                        poster = items[page].poster
                    ) {
                        it.override(20, 20)
                    }
                    Row {
                        PosterImage(
                            modifier = Modifier
                                .height(300.dp)
                                .aspectRatio(7f / 10f)
                                .clip(RoundedCornerShape(16.dp)),
                            poster = items[page].poster
                        )
                        Column(
                            modifier = Modifier
                                .height(300.dp)
                                .padding(16.dp),
                            verticalArrangement = Arrangement.Top
                        ) {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center,
                                text = items[page].name,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Thin,
                                overflow = TextOverflow.Ellipsis
                            )
                            Row(
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                            ) {
                                items[page].tags.forEachIndexed { index, tag ->
                                    Text(
                                        text = tag,
                                        overflow = TextOverflow.Ellipsis,
                                        maxLines = 1,
                                    )
                                    if (index != items[page].tags.lastIndex) Text(" • ")
                                }
                            }
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = items[page].description,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }
        HorizontalPagerIndicator(
            modifier = Modifier.padding(top = 16.dp),
            state = state
        )
    }
}

// ACTUAL OFFSET
fun PagerState.offsetForPage(page: Int) = (currentPage - page) + currentPageOffsetFraction

// OFFSET ONLY FROM THE LEFT
fun PagerState.startOffsetForPage(page: Int): Float {
    return offsetForPage(page).coerceAtLeast(0f)
}

// OFFSET ONLY FROM THE RIGHT
fun PagerState.endOffsetForPage(page: Int): Float {
    return offsetForPage(page).coerceAtMost(0f)
}