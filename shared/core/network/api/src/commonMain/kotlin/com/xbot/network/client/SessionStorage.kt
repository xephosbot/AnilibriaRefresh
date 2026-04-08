package com.xbot.network.client

import io.ktor.client.plugins.auth.providers.BearerTokens

interface SessionStorage {
    suspend fun saveToken(token: String)
    suspend fun getToken(): BearerTokens?
    suspend fun clearToken()
}
