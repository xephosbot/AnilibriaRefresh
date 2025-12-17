package com.xbot.sharedapp

import coil3.PlatformContext
import okio.Path
import okio.Path.Companion.toPath
import java.io.File

internal actual fun getCacheDir(context: PlatformContext): Path {
    val cacheDir = File(System.getProperty("java.io.tmpdir"), "anilibria_cache")
    if (!cacheDir.exists()) {
        cacheDir.mkdirs()
    }
    return cacheDir.absolutePath.toPath()
}
