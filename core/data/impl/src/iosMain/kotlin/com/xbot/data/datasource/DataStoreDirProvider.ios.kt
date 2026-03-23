package com.xbot.data.datasource

import kotlinx.cinterop.ExperimentalForeignApi
import okio.Path.Companion.toPath
import org.koin.core.annotation.Singleton
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask

@Singleton
internal fun iosDataStoreDirProvider(): DataStoreDir {
    @OptIn(ExperimentalForeignApi::class)
    val documentDirectory: NSURL? = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null,
    )
    return DataStoreDir(requireNotNull(documentDirectory).path!!.toPath())
}
