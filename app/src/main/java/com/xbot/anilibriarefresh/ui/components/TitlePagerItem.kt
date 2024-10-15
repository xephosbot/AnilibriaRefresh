package com.xbot.anilibriarefresh.ui.components

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.valentinilk.shimmer.shimmer
import com.xbot.domain.model.TitleModel

@Composable
fun TitlePagerItem(
    modifier: Modifier = Modifier,
    title: TitleModel?
) {
    Crossfade(
        targetState = title,
        label = "" //TODO: информативный label для перехода
    ) { state ->
        when (state) {
            null -> LoadingTitlePagerContent(modifier)
            else -> TitlePagerItemContent(modifier, state)
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun TitlePagerItemContent(
    modifier: Modifier = Modifier,
    title: TitleModel
) {
    val listColors = listOf(Color.Transparent, Color.Transparent, MaterialTheme.colorScheme.surface)
    val brush = Brush.verticalGradient(listColors)

    Box {
        PosterImage(
            poster = title.poster,
            modifier = modifier
                .fillMaxWidth()
                .aspectRatio(7f / 10f)
                .clip(RoundedCornerShape(8.dp))
        )
        Box(modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(7f / 10f)
            .background(brush)
            .align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun LoadingTitlePagerContent(
    modifier: Modifier = Modifier
) {
    val shimmer = LocalShimmer.current

    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(7f / 10f)
            .then(
                if (shimmer != null) Modifier.shimmer(shimmer)
                else Modifier
            )
            .clip(RoundedCornerShape(8.dp))
            .background(Color.LightGray)
    )
}