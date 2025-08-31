package com.xbot.designsystem.utils

import androidx.compose.ui.graphics.ImageBitmap
import coil3.Image
import coil3.PlatformContext

expect fun Image.toComposeImageBitmap(context: PlatformContext): ImageBitmap