package com.xbot.network.api

import arrow.core.Either
import com.xbot.common.error.AppError
import com.xbot.network.client.HttpRequester
import com.xbot.network.models.dto.VastDto
import io.ktor.client.request.get
import org.koin.core.annotation.Singleton

@Singleton
internal class DefaultAdsApi(private val requester: HttpRequester) : AdsApi {
    override suspend fun getVasts(): Either<AppError, List<VastDto>> = requester.request {
        get("ads/vasts")
    }

    override suspend fun getVastsChain(): Either<AppError, String> = requester.request {
        get("ads/vasts/chain")
    }
}
