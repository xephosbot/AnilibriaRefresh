package com.xbot.data.datasource

import android.content.Context

internal class AndroidDataStorePathProvider(private val context: Context) : DataStorePathProvider {
    override fun getPath(fileName: String): String {
        return context.filesDir.resolve(fileName).absolutePath
    }
}
