package com.xbot.network.api

import arrow.core.Either
import com.xbot.common.error.AppError
import com.xbot.network.client.HttpRequester
import com.xbot.network.models.dto.TeamDto
import com.xbot.network.models.dto.TeamRoleDto
import com.xbot.network.models.dto.TeamUserApi
import io.ktor.client.request.get
import org.koin.core.annotation.Singleton

@Singleton
internal class DefaultTeamsApi(private val requester: HttpRequester) : TeamsApi {
    override suspend fun getTeams(): Either<AppError, List<TeamDto>> = requester.request {
        get("teams/")
    }

    override suspend fun getTeamRoles(): Either<AppError, List<TeamRoleDto>> = requester.request {
        get("teams/roles")
    }

    override suspend fun getTeamUsers(): Either<AppError, List<TeamUserApi>> = requester.request {
        get("teams/users")
    }
}
