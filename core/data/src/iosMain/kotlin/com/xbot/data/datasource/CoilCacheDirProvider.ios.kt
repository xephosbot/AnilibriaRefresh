package com.xbot.data.datasource

import com.xbot.network.coil.CoilCacheDir
import okio.Path.Companion.toPath
import org.koin.core.annotation.Singleton
import platform.Foundation.NSCachesDirectory
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSUserDomainMask

@Singleton
internal fun iosCoilCacheDirProvider(): CoilCacheDir {
    val cacheDir = NSSearchPathForDirectoriesInDomains(
        NSCachesDirectory,
        NSUserDomainMask,
        true
    ).first() as String
    return CoilCacheDir(cacheDir.toPath())
}
