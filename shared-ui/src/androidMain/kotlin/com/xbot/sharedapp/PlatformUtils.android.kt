package com.xbot.sharedapp

import coil3.PlatformContext
import okio.Path
import okio.Path.Companion.toPath

internal actual fun getCacheDir(context: PlatformContext): Path {
    return context.cacheDir.absolutePath.toPath()
}
