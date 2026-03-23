package com.xbot.data.datasource

import android.content.Context
import okio.Path.Companion.toOkioPath
import org.koin.core.annotation.Singleton

@Singleton
internal fun androidDataStoreDirProvider(context: Context): DataStoreDir {
    return DataStoreDir(context.filesDir.toOkioPath())
}
