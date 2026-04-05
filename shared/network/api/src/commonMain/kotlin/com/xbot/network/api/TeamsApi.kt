package com.xbot.network.api

import arrow.core.Either
import com.xbot.network.client.NetworkError
import com.xbot.network.models.dto.TeamDto
import com.xbot.network.models.dto.TeamRoleDto
import com.xbot.network.models.dto.TeamUserApi

interface TeamsApi {
    suspend fun getTeams(): Either<NetworkError, List<TeamDto>>
    suspend fun getTeamRoles(): Either<NetworkError, List<TeamRoleDto>>
    suspend fun getTeamUsers(): Either<NetworkError, List<TeamUserApi>>
}
