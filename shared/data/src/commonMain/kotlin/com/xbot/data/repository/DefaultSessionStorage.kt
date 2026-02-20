package com.xbot.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.xbot.network.client.SessionStorage
import io.ktor.client.plugins.auth.providers.BearerTokens
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Singleton

@Singleton
internal class DefaultSessionStorage(
    private val dataStore: DataStore<Preferences>
) : SessionStorage {

    private object Keys {
        val token = stringPreferencesKey("session_access_token")
    }

    val tokenFlow: Flow<String?> = dataStore.data.map { preferences ->
        preferences[Keys.token]
    }

    override suspend fun saveToken(token: String) {
        dataStore.edit { preferences ->
            preferences[Keys.token] = token
        }
    }

    override suspend fun getToken(): BearerTokens? {
        return tokenFlow.firstOrNull()?.let { token ->
            BearerTokens(token, null)
        }
    }

    override suspend fun clearToken() {
        dataStore.edit { preferences ->
            preferences.remove(Keys.token)
        }
    }
}
