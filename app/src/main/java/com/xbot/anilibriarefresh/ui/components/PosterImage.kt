package com.xbot.anilibriarefresh.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import com.xbot.domain.model.PosterModel

@Composable
fun PosterImage(
    modifier: Modifier = Modifier,
    poster: PosterModel,
    contentDescription: String? = null
) {
    AsyncImage(
        modifier = modifier,
        model = poster,
        contentDescription = contentDescription,
        contentScale = ContentScale.Crop
    )
}