package com.xbot.api.client

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map

class SessionManager(
    private val dataStore: DataStore<Preferences>
) {
    val accessToken: Flow<String?> = dataStore.data
        .map { preferences -> preferences[ACCESS_TOKEN] }
    //.filter { token -> token?.isNotEmpty() == true }

    suspend fun setAccessToken(token: String?) {
        dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN] = token ?: ""
        }
    }

    companion object {
        private val ACCESS_TOKEN = stringPreferencesKey("access_token")
    }
}