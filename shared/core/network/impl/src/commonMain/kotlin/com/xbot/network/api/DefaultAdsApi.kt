package com.xbot.network.api

import arrow.core.Either
import com.xbot.domain.models.DomainError
import com.xbot.network.client.ResilientHttpRequester
import com.xbot.network.models.dto.VastDto
import io.ktor.client.request.get
import org.koin.core.annotation.Singleton

@Singleton
internal class DefaultAdsApi(private val requester: ResilientHttpRequester) : AdsApi {
    override suspend fun getVasts(): Either<DomainError, List<VastDto>> = requester.request {
        get("ads/vasts")
    }

    override suspend fun getVastsChain(): Either<DomainError, String> = requester.request {
        get("ads/vasts/chain")
    }
}
