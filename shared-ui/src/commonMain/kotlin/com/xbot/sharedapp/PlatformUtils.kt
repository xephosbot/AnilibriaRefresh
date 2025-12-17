package com.xbot.sharedapp

import coil3.PlatformContext
import okio.Path

internal expect fun getCacheDir(context: PlatformContext): Path
