package com.xbot.designsystem.utils

import androidx.compose.ui.graphics.ImageBitmap
import coil3.Bitmap

expect fun Bitmap.asImageBitmap(): ImageBitmap
