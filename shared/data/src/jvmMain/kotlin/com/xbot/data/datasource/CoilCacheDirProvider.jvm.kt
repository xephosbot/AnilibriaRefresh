package com.xbot.data.datasource

import com.xbot.network.utils.CoilCacheDirProvider
import okio.Path
import okio.Path.Companion.toPath
import java.io.File

internal class JvmCoilCacheDirProvider : CoilCacheDirProvider {
    override fun getCacheDir(): Path {
        return File(System.getProperty("java.io.tmpdir")).absolutePath.toPath()
    }
}
