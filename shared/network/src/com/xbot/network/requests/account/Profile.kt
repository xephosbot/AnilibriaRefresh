package com.xbot.network.requests.account

import com.xbot.network.client.AnilibriaClient
import com.xbot.network.models.entities.accounts.ProfileApi
import io.ktor.client.request.get

suspend fun AnilibriaClient.getProfile(): ProfileApi = request {
    get("/accounts/users/me/profile")
}