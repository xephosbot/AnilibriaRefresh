package com.xbot.designsystem.utils

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asComposeImageBitmap
import coil3.Image
import coil3.PlatformContext
import coil3.annotation.ExperimentalCoilApi
import coil3.toBitmap

@OptIn(ExperimentalCoilApi::class)
actual fun Image.toComposeImageBitmap(context: PlatformContext): ImageBitmap {
    return toBitmap().asComposeImageBitmap()
}