package com.xbot.network.api

import arrow.core.Either
import com.xbot.network.client.NetworkError
import com.xbot.network.client.request
import com.xbot.network.models.dto.ReleaseDto
import io.ktor.client.*
import io.ktor.client.request.*
import org.koin.core.annotation.Singleton

interface SearchApi {
    suspend fun searchReleases(query: String): Either<NetworkError, List<ReleaseDto>>
}

@Singleton
internal class DefaultSearchApi(private val client: HttpClient) : SearchApi {
    override suspend fun searchReleases(query: String): Either<NetworkError, List<ReleaseDto>> = client.request {
        get("app/search/releases") {
            parameter("query", query)
        }
    }
}