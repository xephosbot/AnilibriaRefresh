package com.xbot.anilibriarefresh.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.integration.compose.CrossFade
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.Placeholder
import com.bumptech.glide.integration.compose.Transition
import com.bumptech.glide.integration.compose.placeholder
import com.xbot.domain.model.PosterModel

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PosterImage(
    modifier: Modifier = Modifier,
    poster: PosterModel,
    contentDescription: String? = null,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Crop,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null,
    loading: Placeholder = placeholder(ColorPainter(MaterialTheme.colorScheme.onSurface)),
    failure: Placeholder = placeholder(ColorPainter(MaterialTheme.colorScheme.errorContainer)),
    transition: Transition.Factory = CrossFade
) {
    val requestManager: RequestManager = LocalContext.current.let { remember(it) { Glide.with(it) } }

    GlideImage(
        modifier = modifier,
        model = poster.src,
        contentDescription = contentDescription,
        alignment = alignment,
        contentScale = contentScale,
        alpha = alpha,
        colorFilter = colorFilter,
        loading = loading,
        failure = failure,
        transition = transition
    ) {
        it.thumbnail(
            requestManager.asDrawable()
                .load(poster.thumbnail)
                .override(10, 70)
        )
    }
}