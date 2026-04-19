package com.xbot.network.api

import arrow.core.Either
import com.xbot.domain.models.DomainError
import com.xbot.network.models.dto.ReleaseDto

interface SearchApi {
    suspend fun searchReleases(query: String): Either<DomainError, List<ReleaseDto>>
}
