package com.xbot.network.api

import arrow.core.Either
import com.xbot.domain.models.DomainError
import com.xbot.network.models.dto.TeamDto
import com.xbot.network.models.dto.TeamRoleDto
import com.xbot.network.models.dto.TeamUserApi

interface TeamsApi {
    suspend fun getTeams(): Either<DomainError, List<TeamDto>>
    suspend fun getTeamRoles(): Either<DomainError, List<TeamRoleDto>>
    suspend fun getTeamUsers(): Either<DomainError, List<TeamUserApi>>
}
