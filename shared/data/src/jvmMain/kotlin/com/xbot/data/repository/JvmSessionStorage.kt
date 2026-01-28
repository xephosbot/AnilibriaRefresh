package com.xbot.data.repository

import com.xbot.network.client.SessionStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.prefs.Preferences

internal class JvmSessionStorage : SessionStorage {
    private val preferences = Preferences.userNodeForPackage(JvmSessionStorage::class.java)
    private val key = "auth_token"
    
    private val _tokenFlow = MutableStateFlow(preferences.get(key, null))
    override val tokenFlow: Flow<String?> = _tokenFlow.asStateFlow()

    override suspend fun saveToken(username: String, token: String) {
        preferences.put(key, token)
        _tokenFlow.value = token
    }

    override suspend fun getToken(): String? {
        return preferences.get(key, null)
    }

    override suspend fun clearToken() {
        preferences.remove(key)
        _tokenFlow.value = null
    }
}
