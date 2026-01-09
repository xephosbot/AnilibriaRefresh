package com.xbot.designsystem.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import coil3.SingletonImageLoader
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.SuccessResult
import coil3.toBitmap
import com.materialkolor.ktx.themeColorOrNull
import com.xbot.domain.models.Poster
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun rememberExpressiveSeedColor(
    poster: Poster?,
): State<Color?> {
    val seedColor = remember { mutableStateOf<Color?>(null) }
    val context = LocalPlatformContext.current

    LaunchedEffect(poster) {
        if (poster == null) {
            seedColor.value = null
            return@LaunchedEffect
        }

        withContext(Dispatchers.Default) {
            try {
                val imageLoader = SingletonImageLoader.get(context)
                val request = ImageRequest.Builder(context)
                    .data(poster.src)
                    .build()

                val result = imageLoader.execute(request)
                if (result is SuccessResult) {
                    val bitmap = result.image.toBitmap()
                    seedColor.value = bitmap.asImageBitmap().themeColorOrNull()
                } else {
                    seedColor.value = null
                }
            } catch (_: Exception) {
                seedColor.value = null
            }
        }
    }

    return seedColor
}
