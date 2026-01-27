package com.xbot.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.xbot.network.client.SessionStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

internal class DefaultTokenStorage(
    private val dataStore: DataStore<Preferences>
) : SessionStorage {
    private val key = stringPreferencesKey("auth_token")

    override val tokenFlow: Flow<String?> = dataStore.data
        .map { prefs -> prefs[key] }
        .distinctUntilChanged()

    override suspend fun saveToken(username: String, token: String) {
        dataStore.edit { prefs -> prefs[key] = token }
    }

    override suspend fun getToken(): String? {
        return tokenFlow.firstOrNull()
    }

    override suspend fun clearToken() {
        dataStore.edit { prefs -> prefs.remove(key) }
    }
}
