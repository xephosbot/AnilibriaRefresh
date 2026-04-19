package com.xbot.network.api

import arrow.core.Either
import com.xbot.domain.models.DomainError
import com.xbot.network.client.ResilientHttpRequester
import com.xbot.network.models.dto.TeamDto
import com.xbot.network.models.dto.TeamRoleDto
import com.xbot.network.models.dto.TeamUserApi
import io.ktor.client.request.get
import org.koin.core.annotation.Singleton

@Singleton
internal class DefaultTeamsApi(private val requester: ResilientHttpRequester) : TeamsApi {
    override suspend fun getTeams(): Either<DomainError, List<TeamDto>> = requester.request {
        get("teams/")
    }

    override suspend fun getTeamRoles(): Either<DomainError, List<TeamRoleDto>> = requester.request {
        get("teams/roles")
    }

    override suspend fun getTeamUsers(): Either<DomainError, List<TeamUserApi>> = requester.request {
        get("teams/users")
    }
}
