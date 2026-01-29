package com.xbot.network.client

import kotlinx.coroutines.flow.Flow

interface SessionStorage {
    val tokenFlow: Flow<String?>
    suspend fun saveToken(username: String, token: String)
    suspend fun getToken(): String?
    suspend fun clearToken()
}
