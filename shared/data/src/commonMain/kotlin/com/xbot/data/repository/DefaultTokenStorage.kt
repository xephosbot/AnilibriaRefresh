package com.xbot.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.xbot.network.client.TokenStorage
import kotlinx.coroutines.flow.first

internal class DefaultTokenStorage(
    private val dataStore: DataStore<Preferences>
) : TokenStorage {
    private val key = stringPreferencesKey("auth_token")

    override suspend fun getToken(): String? {
        return dataStore.data.first()[key]
    }

    override suspend fun saveToken(token: String) {
        dataStore.edit { prefs -> prefs[key] = token }
    }

    override suspend fun clearToken() {
        dataStore.edit { prefs -> prefs.remove(key) }
    }
}