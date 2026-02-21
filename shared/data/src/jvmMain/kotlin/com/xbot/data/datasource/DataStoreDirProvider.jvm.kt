package com.xbot.data.datasource

import okio.Path.Companion.toPath
import org.koin.core.annotation.Singleton
import java.io.File

@Singleton
internal fun jvmDataStoreDirProvider(): DataStoreDir {
    return DataStoreDir(File(System.getProperty("java.io.tmpdir")).absolutePath.toPath())
}
