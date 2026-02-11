package com.xbot.data.datasource

import android.content.Context
import com.xbot.network.utils.CoilCacheDirProvider
import okio.Path
import okio.Path.Companion.toOkioPath

internal class AndroidCoilCacheDirProvider(private val context: Context) : CoilCacheDirProvider {
    override fun getCacheDir(): Path {
        return context.cacheDir.toOkioPath()
    }
}
