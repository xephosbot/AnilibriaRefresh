package com.xbot.data.datasource

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import okio.Path
import org.koin.core.annotation.Singleton
import kotlin.jvm.JvmInline

@Singleton
internal fun createDataStore(
    cacheDir: DataStoreDir,
): DataStore<Preferences> = PreferenceDataStoreFactory.createWithPath(
    produceFile = { cacheDir.path.resolve(dataStoreFileName) }
)

@JvmInline
internal value class DataStoreDir(val path: Path)

internal const val dataStoreFileName = "app.preferences_pb"
