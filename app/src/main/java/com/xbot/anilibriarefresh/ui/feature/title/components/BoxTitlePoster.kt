package com.xbot.anilibriarefresh.ui.feature.title.components

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.xbot.anilibriarefresh.ui.components.PosterImage
import com.xbot.domain.model.TitleDetailModel

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun BoxTitlePoster(
    title: TitleDetailModel,
    fadeGradient: Brush,
    scrollState: ScrollState
) {
    Box(
        modifier = Modifier
            .graphicsLayer {
                alpha = 1f-(scrollState.value.toFloat() / size.height).coerceIn(0f, 1f)
                translationY = scrollState.value.toFloat()
            }
            .wrapContentSize()
            .clipToBounds()
    ) {
        PosterImage(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(7f / 10f),
            poster = title.poster
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(7f / 10f)
                .background(fadeGradient)
        )
    }
}