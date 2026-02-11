package com.xbot.data.datasource

import com.xbot.network.utils.CoilCacheDirProvider
import okio.Path
import okio.Path.Companion.toPath
import platform.Foundation.NSCachesDirectory
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSUserDomainMask

internal class IosCoilCacheDirProvider : CoilCacheDirProvider {
    override fun getCacheDir(): Path {
        val cacheDir = NSSearchPathForDirectoriesInDomains(
            NSCachesDirectory,
            NSUserDomainMask,
            true
        ).first() as String
        return cacheDir.toPath()
    }
}
