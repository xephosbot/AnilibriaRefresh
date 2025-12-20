package com.xbot.network.client

import kotlinx.coroutines.flow.Flow

interface TokenStorage {
    val tokenFlow: Flow<String?>
    suspend fun saveToken(token: String)
    suspend fun clearToken()
}