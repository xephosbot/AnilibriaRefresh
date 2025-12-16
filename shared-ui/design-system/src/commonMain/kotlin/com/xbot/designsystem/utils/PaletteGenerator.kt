package com.xbot.designsystem.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ImageBitmap
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.SuccessResult
import coil3.toBitmap
import com.kmpalette.loader.ImageBitmapLoader
import com.kmpalette.palette.graphics.Palette
import com.kmpalette.rememberPaletteState
import com.xbot.domain.models.Poster

@Composable
fun rememberPosterPalette(
    poster: Poster?,
): Palette? {
    val context = LocalPlatformContext.current
    val imageLoader = SingletonImageLoader.get(context)
    
    val loader = remember(imageLoader, context) { 
        CoilLoader(imageLoader, context) 
    }
    
    val paletteState = rememberPaletteState(
        loader = loader,
    )
    
    LaunchedEffect(poster, paletteState) {
        if (poster != null) {
            paletteState.generate(poster.src)
        } else {
            paletteState.reset()
        }
    }
    
    return paletteState.palette
}

internal class CoilLoader(
    private val loader: ImageLoader,
    private val context: PlatformContext,
) : ImageBitmapLoader<String> {
    override suspend fun load(input: String): ImageBitmap {
        val request = ImageRequest.Builder(context)
            .data(input)
            .build()
            
        val result = loader.execute(request)
        val image = (result as? SuccessResult)?.image ?: throw Exception("Failed to load image")
        return image.toBitmap().asImageBitmap()
    }
}
