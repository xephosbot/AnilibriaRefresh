package com.xbot.network.api

import arrow.core.Either
import com.xbot.domain.models.DomainError
import com.xbot.network.models.dto.VastDto

interface AdsApi {
    suspend fun getVasts(): Either<DomainError, List<VastDto>>
    suspend fun getVastsChain(): Either<DomainError, String>
}
