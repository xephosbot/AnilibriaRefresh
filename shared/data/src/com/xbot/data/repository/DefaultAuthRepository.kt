package com.xbot.data.repository

import com.xbot.data.mapper.toApi
import com.xbot.domain.models.enums.SocialType
import com.xbot.domain.repository.AuthRepository
import com.xbot.network.client.AnilibriaClient
import com.xbot.network.requests.account.forgotPassword
import com.xbot.network.requests.account.login
import com.xbot.network.requests.account.logout
import com.xbot.network.requests.account.resetPassword
import com.xbot.network.requests.account.socialAuthenticate
import com.xbot.network.requests.account.socialLogin
import kotlinx.coroutines.flow.Flow

internal class DefaultAuthRepository(
    private val client: AnilibriaClient
): AuthRepository {
    override suspend fun login(login: String, password: String): Result<Unit> = runCatching {
        val token = client.login(login, password).token
        client.setAuthToken(token)
    }

    override suspend fun logout(): Result<Unit> = runCatching {
        client.logout()
        client.clearAuth()
    }

    override suspend fun socialLogin(provider: SocialType): Result<Unit> = runCatching {
        val response = client.socialLogin(provider.toApi())
        val token = client.socialAuthenticate(response.state).token
        client.setAuthToken(token)
    }

    override suspend fun forgotPassword(email: String): Result<Unit> = runCatching {
        client.forgotPassword(email)
    }

    override suspend fun resetPassword(
        token: String,
        password: String,
        passwordConfirmation: String
    ): Result<Unit> = runCatching {
        client.resetPassword(token, password, passwordConfirmation)
    }

    override fun observeAuthState(): Flow<Boolean> = client.observeAuthState()
}