package com.xbot.shared.data.sources.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.xbot.shared.data.sources.remote.TokenProvider
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

class TokenDataStore(private val dataStore: DataStore<Preferences>) : TokenProvider {

    override suspend fun getToken(): String? {
        return dataStore.data.map { preferences ->
            preferences[ACCESS_TOKEN]
        }.firstOrNull()
    }

    override suspend fun saveToken(token: String) {
        dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN] = token
        }
    }

    override suspend fun clearToken() {
        dataStore.edit { preferences ->
            preferences.remove(ACCESS_TOKEN)
        }
    }

    companion object {
        private val ACCESS_TOKEN = stringPreferencesKey("access_token")
    }
}