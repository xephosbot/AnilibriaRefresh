package com.xbot.data.datasource

import java.io.File

internal class JvmDataStorePathProvider : DataStorePathProvider {
    override fun getPath(fileName: String): String {
        val file = File(System.getProperty("java.io.tmpdir"), fileName)
        return file.absolutePath
    }
}
