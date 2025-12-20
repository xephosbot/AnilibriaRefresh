package com.xbot.network.client

import io.ktor.client.plugins.cookies.CookiesStorage
import io.ktor.http.Cookie
import io.ktor.http.Url
import io.ktor.http.decodeURLQueryComponent
import kotlinx.coroutines.flow.firstOrNull

internal class SessionCookieStorage(
    private val tokenStorage: TokenStorage
) : CookiesStorage {

    override suspend fun addCookie(requestUrl: Url, cookie: Cookie) {
        if (cookie.name != SESSION_COOKIE) return

        if (cookie.value == "deleted") {
            tokenStorage.clearToken()
            return
        }

        val current = tokenStorage.tokenFlow.firstOrNull()
        if (current == null) {
            tokenStorage.saveToken(cookie.value.decodeURLQueryComponent())
        }
    }

    override suspend fun get(requestUrl: Url): List<Cookie> {
        val token = tokenStorage.tokenFlow.firstOrNull() ?: return emptyList()

        return listOf(
            Cookie(
                name = SESSION_COOKIE,
                value = token,
                path = "/",
            )
        )
    }

    override fun close() {}

    companion object {
        private const val SESSION_COOKIE = "ANILIBRIA_API_SESSION"
    }
}