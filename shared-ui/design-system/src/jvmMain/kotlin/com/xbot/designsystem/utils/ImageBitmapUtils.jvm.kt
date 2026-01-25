package com.xbot.designsystem.utils

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asComposeImageBitmap
import coil3.Bitmap

actual fun Bitmap.asImageBitmap(): ImageBitmap = this.asComposeImageBitmap()
