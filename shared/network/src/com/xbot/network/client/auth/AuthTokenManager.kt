package com.xbot.network.client.auth

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

/**
 * Manager for handling authentication tokens using DataStore.
 */
class AuthTokenManager(
    private val dataStore: DataStore<Preferences>
) {
    private val tokenKey = stringPreferencesKey("auth_token")

    /**
     * Flow of the current authentication token
     */
    val authToken: Flow<String?> = dataStore.data.map { preferences ->
        preferences[tokenKey]
    }

    /**
     * Get current token as suspend function
     */
    suspend fun getToken(): String? {
        return authToken.firstOrNull()
    }

    /**
     * Save authentication token
     */
    suspend fun saveToken(token: String) {
        dataStore.edit { preferences ->
            preferences[tokenKey] = token
        }
    }

    /**
     * Clear authentication token
     */
    suspend fun clearToken() {
        dataStore.edit { preferences ->
            preferences.remove(tokenKey)
        }
    }

    /**
     * Check if user is authenticated
     */
    suspend fun isAuthenticated(): Boolean {
        return getToken() != null
    }
}