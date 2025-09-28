package com.xbot.data.datasource

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import java.io.File

internal fun createDataStore(): DataStore<Preferences> = createDataStore(
    producePath = {
        val file = File(System.getProperty("java.io.tmpdir"), dataStoreFileName)
        file.absolutePath
    }
)