package com.xbot.anilibriarefresh.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import coil3.compose.SubcomposeAsyncImage
import com.xbot.anilibriarefresh.ui.utils.LocalShimmer
import com.xbot.anilibriarefresh.ui.utils.shimmerSafe
import com.xbot.domain.model.PosterModel

@Composable
fun PosterImage(
    modifier: Modifier = Modifier,
    poster: PosterModel,
    contentDescription: String? = null
) {
    val shimmer = LocalShimmer.current

    SubcomposeAsyncImage (
        modifier = modifier,
        model = poster,
        contentDescription = contentDescription,
        contentScale = ContentScale.Crop,
        loading = {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .shimmerSafe(shimmer)
                    .background(Color.LightGray)
            )
        }
    )
}