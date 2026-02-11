package com.xbot.network.utils

import okio.Path

interface CoilCacheDirProvider {
    fun getCacheDir(): Path
}
