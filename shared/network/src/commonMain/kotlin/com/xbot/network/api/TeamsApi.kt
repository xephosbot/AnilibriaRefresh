package com.xbot.network.api

import arrow.core.Either
import com.xbot.network.client.NetworkError
import com.xbot.network.client.request
import com.xbot.network.models.dto.TeamDto
import com.xbot.network.models.dto.TeamRoleDto
import com.xbot.network.models.dto.TeamUserApi
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import org.koin.core.annotation.Singleton

interface TeamsApi {
    suspend fun getTeams(): Either<NetworkError, List<TeamDto>>
    suspend fun getTeamRoles(): Either<NetworkError, List<TeamRoleDto>>
    suspend fun getTeamUsers(): Either<NetworkError, List<TeamUserApi>>
}

@Singleton
internal class DefaultTeamsApi(private val client: HttpClient) : TeamsApi {
    override suspend fun getTeams(): Either<NetworkError, List<TeamDto>> = client.request {
        get("teams/")
    }

    override suspend fun getTeamRoles(): Either<NetworkError, List<TeamRoleDto>> = client.request {
        get("teams/roles")
    }

    override suspend fun getTeamUsers(): Either<NetworkError, List<TeamUserApi>> = client.request {
        get("teams/users")
    }
}