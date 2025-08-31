package com.xbot.designsystem.utils

import android.content.Context
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.graphics.drawable.toBitmap
import coil3.Image
import coil3.PlatformContext
import coil3.annotation.ExperimentalCoilApi
import coil3.asDrawable

@OptIn(ExperimentalCoilApi::class)
actual fun Image.toComposeImageBitmap(context: PlatformContext): ImageBitmap {
    return asDrawable((context as Context).resources).toBitmap().asImageBitmap()
}