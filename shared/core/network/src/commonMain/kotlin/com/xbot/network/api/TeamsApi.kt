package com.xbot.network.api

import arrow.core.Either
import com.xbot.common.error.AppError
import com.xbot.network.models.dto.TeamDto
import com.xbot.network.models.dto.TeamRoleDto
import com.xbot.network.models.dto.TeamUserApi
import de.jensklingenberg.ktorfit.http.GET

interface TeamsApi {
    @GET("teams/")
    suspend fun getTeams(): Either<AppError, List<TeamDto>>

    @GET("teams/roles")
    suspend fun getTeamRoles(): Either<AppError, List<TeamRoleDto>>

    @GET("teams/users")
    suspend fun getTeamUsers(): Either<AppError, List<TeamUserApi>>
}
