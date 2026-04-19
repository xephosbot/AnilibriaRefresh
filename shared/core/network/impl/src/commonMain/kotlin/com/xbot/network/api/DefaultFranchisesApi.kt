package com.xbot.network.api

import arrow.core.Either
import com.xbot.domain.models.DomainError
import com.xbot.network.client.ResilientHttpRequester
import com.xbot.network.models.dto.FranchiseDto
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import org.koin.core.annotation.Singleton

@Singleton
internal class DefaultFranchisesApi(private val requester: ResilientHttpRequester) : FranchisesApi {
    override suspend fun getFranchises(): Either<DomainError, List<FranchiseDto>> = requester.request {
        get("anime/franchises")
    }

    override suspend fun getFranchise(franchiseId: String): Either<DomainError, FranchiseDto> = requester.request {
        get("anime/franchises/${franchiseId}")
    }

    override suspend fun getFranchisesRandom(limit: Int): Either<DomainError, List<FranchiseDto>> = requester.request {
        get("anime/franchises/random") {
            parameter("limit", limit)
        }
    }

    override suspend fun getFranchisesByRelease(releaseId: Int): Either<DomainError, List<FranchiseDto>> = requester.request {
        get("anime/franchises/release/${releaseId}")
    }

}
