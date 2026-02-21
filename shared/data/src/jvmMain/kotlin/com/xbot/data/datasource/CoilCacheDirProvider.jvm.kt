package com.xbot.data.datasource

import com.xbot.network.coil.CoilCacheDir
import okio.Path.Companion.toPath
import org.koin.core.annotation.Singleton
import java.io.File

@Singleton
internal fun jvmCoilCacheDirProvider(): CoilCacheDir {
    return CoilCacheDir(File(System.getProperty("java.io.tmpdir")).absolutePath.toPath())
}
