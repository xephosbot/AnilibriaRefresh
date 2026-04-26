package com.xbot.network.client

import arrow.resilience.Schedule
import com.xbot.domain.models.DomainError
import com.xbot.network.plugins.ConnectivityGate
import dev.jordond.connectivity.Connectivity
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail
import kotlin.time.Duration.Companion.milliseconds

class ResilientHttpRequesterTest {

    private val json = Json { ignoreUnknownKeys = true }
    private val jsonHeader = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())

    // Manual mock of Connectivity
    private class FakeConnectivity(private val initialStatus: Connectivity.Status) : Connectivity {
        private val _statusUpdates = MutableSharedFlow<Connectivity.Status>(replay = 1).apply {
            tryEmit(initialStatus)
        }
        override val statusUpdates: SharedFlow<Connectivity.Status> = _statusUpdates.asSharedFlow()
        override val monitoring: StateFlow<Boolean> = MutableStateFlow(true)
        override fun start() {}
        override fun stop() {}
        override suspend fun status(): Connectivity.Status = initialStatus
    }

    private fun createTestClient(
        engine: MockEngine,
        connectivity: Connectivity
    ): HttpClient = HttpClient(engine) {
        expectSuccess = true
        install(ContentNegotiation) {
            json(json)
        }
        install(ConnectivityGate) {
            this.connectivity = connectivity
        }
        install(HttpTimeout)
    }

    private val fastSchedule = Schedule.recurs<DomainError>(2)
        .zipLeft(Schedule.spaced(1.milliseconds))
        .doWhile { error, _ -> error.isRetryable }

    @Test
    fun `should return success on first attempt`() = runTest {
        val engine = MockEngine { respond("{\"id\": 1}", HttpStatusCode.OK, jsonHeader) }
        val connectivity = FakeConnectivity(Connectivity.Status.Connected(metered = false))
        val client = createTestClient(engine, connectivity)
        val requester = ResilientHttpRequester(client, fastSchedule)

        val result = requester.request<TestDto> { get("test") }

        assertTrue(result.isRight())
    }

    @Test
    fun `should retry on 500 and eventually succeed`() = runTest {
        var attempts = 0
        val engine = MockEngine {
            attempts++
            if (attempts < 2) respond("", HttpStatusCode.InternalServerError)
            else respond("{\"id\": 1}", HttpStatusCode.OK, jsonHeader)
        }
        val connectivity = FakeConnectivity(Connectivity.Status.Connected(metered = false))
        val client = createTestClient(engine, connectivity)
        val requester = ResilientHttpRequester(client, fastSchedule)

        val result = requester.request<TestDto> { get("test") }

        result.onLeft { fail("Expected Right but got Left: $it") }
        assertTrue(attempts == 2)
    }

    @Test
    fun `should fail immediately on 401`() = runTest {
        var attempts = 0
        val engine = MockEngine {
            attempts++
            respond("", HttpStatusCode.Unauthorized)
        }
        val connectivity = FakeConnectivity(Connectivity.Status.Connected(metered = false))
        val client = createTestClient(engine, connectivity)
        val requester = ResilientHttpRequester(client, fastSchedule)

        val result = requester.request<TestDto> { get("test") }

        assertTrue(result.isLeft())
        assertEquals(attempts, 1)
    }

    @Test
    fun `should fail immediately when offline via ConnectivityGate`() = runTest {
        val engine = MockEngine { respond("{}", HttpStatusCode.OK) }
        val connectivity = FakeConnectivity(Connectivity.Status.Disconnected)
        val client = createTestClient(engine, connectivity)
        val requester = ResilientHttpRequester(client, fastSchedule)

        val result = requester.request<TestDto> { get("test") }

        assertTrue(result.isLeft())
        result.onLeft { error ->
            assertTrue(error is DomainError.NoConnection)
        }
    }
}

@kotlinx.serialization.Serializable
data class TestDto(val id: Int)
