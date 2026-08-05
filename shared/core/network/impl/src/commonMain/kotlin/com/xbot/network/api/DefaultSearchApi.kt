package com.xbot.network.api

import arrow.core.Either
import com.xbot.common.error.AppError
import com.xbot.network.client.HttpRequester
import com.xbot.network.models.dto.ReleaseDto
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import org.koin.core.annotation.Singleton

@Singleton
internal class DefaultSearchApi(private val requester: HttpRequester) : SearchApi {
    override suspend fun searchReleases(query: String): Either<AppError, List<ReleaseDto>> = requester.request {
        get("app/search/releases") {
            parameter("query", query)
        }
    }
}
