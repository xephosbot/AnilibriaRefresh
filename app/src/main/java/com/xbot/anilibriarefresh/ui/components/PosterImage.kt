package com.xbot.anilibriarefresh.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import com.xbot.anilibriarefresh.models.Poster
import com.xbot.anilibriarefresh.ui.utils.LocalShimmer
import com.xbot.anilibriarefresh.ui.utils.shimmerSafe

@Composable
fun PosterImage(
    modifier: Modifier = Modifier,
    poster: Poster,
    contentDescription: String? = null,
    contentScale: ContentScale = ContentScale.Crop
) {
    var isLoading by rememberSaveable(poster) { mutableStateOf(true) }
    val shimmer = LocalShimmer.current

    Box(
        modifier = modifier
    ) {
        AsyncImage(
            modifier = Modifier.fillMaxSize(),
            model = poster,
            contentDescription = contentDescription,
            contentScale = contentScale,
            onSuccess = {
                isLoading = false
            }
        )
        AnimatedVisibility(
            visible = isLoading,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .shimmerSafe(shimmer)
                    .background(Color.LightGray)
            )
        }
    }
}