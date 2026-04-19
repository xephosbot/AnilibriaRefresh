package com.xbot.network.api

import arrow.core.Either
import com.xbot.domain.models.DomainError
import com.xbot.network.client.ResilientHttpRequester
import com.xbot.network.models.dto.ReleaseDto
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import org.koin.core.annotation.Singleton

@Singleton
internal class DefaultSearchApi(private val requester: ResilientHttpRequester) : SearchApi {
    override suspend fun searchReleases(query: String): Either<DomainError, List<ReleaseDto>> = requester.request {
        get("app/search/releases") {
            parameter("query", query)
        }
    }
}
