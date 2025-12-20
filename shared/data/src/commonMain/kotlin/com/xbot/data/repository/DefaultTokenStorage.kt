package com.xbot.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.xbot.network.client.TokenStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

internal class DefaultTokenStorage(
    private val dataStore: DataStore<Preferences>
) : TokenStorage {
    private val key = stringPreferencesKey("auth_token")

    override val tokenFlow: Flow<String?> = dataStore.data
        .catch { emit(emptyPreferences()) }
        .map { prefs -> prefs[key] }
        .distinctUntilChanged()

    override suspend fun saveToken(token: String) {
        dataStore.edit { prefs -> prefs[key] = token }
    }

    override suspend fun clearToken() {
        dataStore.edit { prefs -> prefs.remove(key) }
    }
}