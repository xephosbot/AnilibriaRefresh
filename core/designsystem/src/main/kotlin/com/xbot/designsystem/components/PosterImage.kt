package com.xbot.designsystem.components

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil3.compose.AsyncImage
import coil3.compose.rememberAsyncImagePainter
import com.xbot.designsystem.R
import com.xbot.designsystem.modifier.LocalShimmer
import com.xbot.designsystem.modifier.shimmerSafe
import com.xbot.domain.models.Poster

@Composable
fun PosterImage(
    poster: Poster?,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    contentScale: ContentScale = ContentScale.Crop,
    placeholder: Painter? = null,
) {
    val shimmer = LocalShimmer.current
    var isLoading by rememberSaveable(poster) { mutableStateOf(true) }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        AsyncImage(
            modifier = Modifier.fillMaxSize(),
            model = poster,
            placeholder = rememberAsyncImagePainter(poster?.thumbnail),
            error = placeholder ?: painterResource(R.drawable.placeholder_default),
            contentDescription = contentDescription,
            contentScale = contentScale,
            onLoading = {
                isLoading = true
            },
            onSuccess = {
                isLoading = false
            },
            onError = {
                isLoading = false
            }
        )
        AnimatedVisibility(
            visible = isLoading,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .shimmerSafe(shimmer)
                    .background(Color.LightGray),
            )
        }
    }
}
