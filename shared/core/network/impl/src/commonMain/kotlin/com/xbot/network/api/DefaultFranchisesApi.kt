package com.xbot.network.api

import arrow.core.Either
import com.xbot.common.error.AppError
import com.xbot.network.client.HttpRequester
import com.xbot.network.models.dto.FranchiseDto
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import org.koin.core.annotation.Singleton

@Singleton
internal class DefaultFranchisesApi(private val requester: HttpRequester) : FranchisesApi {
    override suspend fun getFranchises(): Either<AppError, List<FranchiseDto>> = requester.request {
        get("anime/franchises")
    }

    override suspend fun getFranchise(franchiseId: String): Either<AppError, FranchiseDto> = requester.request {
        get("anime/franchises/${franchiseId}")
    }

    override suspend fun getFranchisesRandom(limit: Int): Either<AppError, List<FranchiseDto>> = requester.request {
        get("anime/franchises/random") {
            parameter("limit", limit)
        }
    }

    override suspend fun getFranchisesByRelease(releaseId: Int): Either<AppError, List<FranchiseDto>> = requester.request {
        get("anime/franchises/release/${releaseId}")
    }

}
