package com.xbot.data.datasource

import android.content.Context
import com.xbot.network.coil.CoilCacheDir
import okio.Path.Companion.toOkioPath
import org.koin.core.annotation.Singleton

@Singleton
internal fun androidCoilCacheDirProvider(context: Context) : CoilCacheDir {
    return CoilCacheDir(context.cacheDir.toOkioPath())
}
