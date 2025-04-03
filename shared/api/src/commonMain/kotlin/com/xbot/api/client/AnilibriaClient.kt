package com.xbot.api.client

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse

/**
 * Client for interacting with the Anilibria API.
 *
 * This class provides a convenient way to make requests to the Anilibria API.
 * It handles the underlying HTTP communication, content negotiation, and error handling.
 *
 * @see <a href="https://anilibria.top/api/docs/v1">Anilibria API Documentation</a>
 */
class AnilibriaClient(private val client: HttpClient) {
    /**
     * Executes a request using the provided [action] and deserializes the response body to type [T].
     *
     * This function is a helper for making requests using an [HttpClient] instance.
     * It internally uses `HttpResponse.body<T>()` to deserialize the response.
     *
     * @param T The type to which the response body should be deserialized.
     * @param action A lambda function that defines the request to be executed.
     *               This lambda takes an [HttpClient] as a receiver and returns an [HttpResponse].
     * @return The deserialized response body of type [T].
     * @throws Exception if the request fails or the response body cannot be deserialized.
     */
    internal suspend inline fun <reified T> request(
        action: HttpClient.() -> HttpResponse
    ): T {
        return client.action().body<T>()
    }
}