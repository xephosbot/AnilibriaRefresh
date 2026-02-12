package com.xbot.data.datasource

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import okio.Path
import kotlin.jvm.JvmInline

internal fun createDataStore(producePath: () -> Path): DataStore<Preferences> =
    PreferenceDataStoreFactory.createWithPath(
        produceFile = { producePath() }
    )

@JvmInline
internal value class DataStoreDir(val path: Path)

internal const val dataStoreFileName = "app.preferences_pb"
