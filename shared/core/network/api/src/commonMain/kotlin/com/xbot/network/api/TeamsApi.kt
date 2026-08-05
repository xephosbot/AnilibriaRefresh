package com.xbot.network.api

import arrow.core.Either
import com.xbot.common.error.AppError
import com.xbot.network.models.dto.TeamDto
import com.xbot.network.models.dto.TeamRoleDto
import com.xbot.network.models.dto.TeamUserApi

interface TeamsApi {
    suspend fun getTeams(): Either<AppError, List<TeamDto>>
    suspend fun getTeamRoles(): Either<AppError, List<TeamRoleDto>>
    suspend fun getTeamUsers(): Either<AppError, List<TeamUserApi>>
}
