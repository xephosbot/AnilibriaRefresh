package com.xbot.network.api

import com.xbot.logger.AppLogger
import com.xbot.network.client.EitherConverterFactory
import de.jensklingenberg.ktorfit.Ktorfit
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.Url
import io.ktor.http.headersOf
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlinx.coroutines.test.runTest

// The API keeps only the last value of a repeated plain key; lists must go as `key[]`.
class ListQueryParamsTest {

    private var requestedUrl: Url? = null

    private val api: ReleasesApi = run {
        val client = HttpClient(
            MockEngine { request ->
                requestedUrl = request.url
                respond(
                    "{}",
                    HttpStatusCode.OK,
                    headersOf(HttpHeaders.ContentType, "application/json")
                )
            }
        )
        Ktorfit.Builder()
            .baseUrl("https://example.com/")
            .httpClient(client)
            .converterFactories(EitherConverterFactory(lazy { NoOpLogger }))
            .build()
            .createReleasesApi()
    }

    @Test
    fun listIsSentAsArrayParameters() = runTest {
        api.getReleasesList(ids = listOf(1, 2, 3))

        assertEquals(listOf("1", "2", "3"), requestedUrl?.parameters?.getAll("ids[]"))
        assertNull(requestedUrl?.parameters?.getAll("ids"))
    }

    @Test
    fun emptyListIsOmitted() = runTest {
        api.getReleasesList(ids = emptyList())

        assertNull(requestedUrl?.parameters?.getAll("ids[]"))
    }

    private object NoOpLogger : AppLogger {
        override fun log(message: String, tag: String) = Unit
        override fun reportError(throwable: Throwable, message: String?) = Unit
    }
}
