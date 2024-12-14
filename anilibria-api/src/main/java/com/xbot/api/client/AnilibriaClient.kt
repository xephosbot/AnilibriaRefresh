package com.xbot.api.client

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse

/**
 * Клиент для взаимодействия с API Anilibria.
 * @see <a href="https://anilibria.top/api/docs/v1">Документация API Anilibria</a>
 */
class AnilibriaClient(
    val client: HttpClient
) {
    suspend inline fun <reified T> AnilibriaClient.request(
        action: HttpClient.() -> HttpResponse
    ): T {
        return client.action().body<T>()
    }
}