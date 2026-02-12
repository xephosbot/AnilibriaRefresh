package com.xbot.data.datasource

import com.xbot.network.coil.CoilCacheDir
import kotlinx.cinterop.ExperimentalForeignApi
import okio.Path.Companion.toPath
import platform.Foundation.NSCachesDirectory
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSUserDomainMask

internal fun iosCoilCacheDirProvider(): CoilCacheDir {
    val cacheDir = NSSearchPathForDirectoriesInDomains(
        NSCachesDirectory,
        NSUserDomainMask,
        true
    ).first() as String
    return CoilCacheDir(cacheDir.toPath())
}
