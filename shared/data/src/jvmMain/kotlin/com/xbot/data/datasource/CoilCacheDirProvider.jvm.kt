package com.xbot.data.datasource

import com.xbot.network.coil.CoilCacheDir
import okio.Path.Companion.toPath
import java.io.File

internal fun jvmCoilCacheDirProvider(): CoilCacheDir {
    return CoilCacheDir(File(System.getProperty("java.io.tmpdir")).absolutePath.toPath())
}
