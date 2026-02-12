package com.xbot.data.datasource

import okio.Path.Companion.toPath
import java.io.File

internal fun jvmDataStoreDirProvider(): DataStoreDir {
    return DataStoreDir(File(System.getProperty("java.io.tmpdir")).absolutePath.toPath())
}
