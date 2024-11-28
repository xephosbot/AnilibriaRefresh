package com.xbot.anilibriarefresh.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.carousel.CarouselItemScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import com.xbot.anilibriarefresh.models.Title
import com.xbot.anilibriarefresh.ui.utils.LocalShimmer
import com.xbot.anilibriarefresh.ui.utils.dimmedEdge
import com.xbot.anilibriarefresh.ui.utils.shimmerSafe

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarouselItemScope.TitleCarouselItem(
    modifier: Modifier = Modifier,
    title: Title,
    onClick: (Title) -> Unit,
) {
    val isCompact = LocalConfiguration.current.screenWidthDp.dp < 600.dp

    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(if (isCompact) 7f / 10f else 10f / 6f)
            .maskClip(CarouselItemShape)
            .clickable { onClick(title) },
        contentAlignment = Alignment.BottomStart
    ) {
        PosterImage(
            modifier = Modifier
                .fillMaxSize()
                .dimmedEdge(edgeHeightRatio = 0.75f),
            poster = title.poster,
        )
        when {
            isCompact -> CompactItemLayout(title = title)
            else -> LargeItemLayout(title = title)
        }
    }
}

@Composable
fun LoadingCarouselItem(
    modifier: Modifier = Modifier,
) {
    val shimmer = LocalShimmer.current
    val isCompact = LocalConfiguration.current.screenWidthDp.dp < 600.dp

    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(if (isCompact) 7f / 10f else 10f / 6f)
            .shimmerSafe(shimmer)
            .clip(RoundedCornerShape(24.dp))
            .background(Color.LightGray),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CarouselItemScope.CompactItemLayout(
    modifier: Modifier = Modifier,
    title: Title
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .graphicsLayer {
                val fraction =
                    ((carouselItemInfo.size - carouselItemInfo.maxSize / 2) / (carouselItemInfo.maxSize / 2))
                        .coerceIn(0f, 1f)
                alpha = lerp(0f, 1f, fraction)
                translationX = carouselItemInfo.maskRect.left
            },
        contentAlignment = Alignment.BottomStart
    ) {
        Row(
            verticalAlignment = Alignment.Bottom
        ) {
            Column(
                modifier = Modifier.weight(1f),
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    title.tags.forEach { tag ->
                        TagChip(
                            tag = tag,
                            containerColor = Color.White,
                            contentColor = Color.Black
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = title.name,
                    style = LocalTextStyle.current.copy(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 28.sp,
                        color = Color.White
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = title.description,
                    style = LocalTextStyle.current.copy(
                        fontSize = 12.sp,
                        lineHeight = 18.sp,
                        color = Color.White
                    ),
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            FilledIconButton(
                modifier = Modifier.size(48.dp),
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = Color.White
                ),
                onClick = {}
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    tint = Color.Black,
                    contentDescription = null
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CarouselItemScope.LargeItemLayout(
    modifier: Modifier = Modifier,
    title: Title
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .graphicsLayer {
                val fraction =
                    ((carouselItemInfo.size - carouselItemInfo.maxSize / 2) / (carouselItemInfo.maxSize / 2))
                        .coerceIn(0f, 1f)
                alpha = lerp(0f, 1f, fraction)
                translationX = carouselItemInfo.maskRect.left
            },
        contentAlignment = Alignment.BottomStart
    ) {
        Row(
            verticalAlignment = Alignment.Bottom
        ) {
            Column(
                modifier = Modifier.weight(1f),
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    title.tags.forEach { tag ->
                        TagChip(
                            tag = tag,
                            containerColor = Color.White,
                            contentColor = Color.Black
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = title.name,
                    style = LocalTextStyle.current.copy(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 28.sp,
                        color = Color.White
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = title.description,
                    style = LocalTextStyle.current.copy(
                        fontSize = 12.sp,
                        lineHeight = 18.sp,
                        color = Color.White
                    ),
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            FilledIconButton(
                modifier = Modifier.size(48.dp),
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = Color.White
                ),
                onClick = {}
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    tint = Color.Black,
                    contentDescription = null
                )
            }
        }
    }
}

private val CarouselItemShape = RoundedCornerShape(24.dp)
