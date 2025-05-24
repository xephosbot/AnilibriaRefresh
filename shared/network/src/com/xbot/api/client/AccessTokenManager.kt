package com.xbot.api.client

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

class AccessTokenManager(
    private val dataStore: DataStore<Preferences>
) {
    suspend fun getAccessToken(): String? {
        return dataStore.data
            .map { preferences -> preferences[ACCESS_TOKEN] }
            .filter { token -> token?.isNotEmpty() == true }
            .firstOrNull()
    }

    suspend fun setAccessToken(token: String?) {
        dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN] = token ?: ""
        }
    }

    companion object {
        private val ACCESS_TOKEN = stringPreferencesKey("access_token")
    }
}