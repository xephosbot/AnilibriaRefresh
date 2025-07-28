package com.xbot.network.requests.teams

import com.xbot.network.client.AnilibriaClient
import com.xbot.network.models.entities.teams.TeamApi
import com.xbot.network.models.entities.teams.TeamRoleApi
import com.xbot.network.models.entities.teams.TeamUserApi
import io.ktor.client.request.get

suspend fun AnilibriaClient.getTeams(): List<TeamApi> = request {
    get("teams/")
}

suspend fun AnilibriaClient.getTeamRoles(): List<TeamRoleApi> = request {
    get("teams/roles")
}

suspend fun AnilibriaClient.getTeamUsers(): List<TeamUserApi> = request {
    get("teams/users")
}