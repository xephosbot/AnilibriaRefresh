package com.xbot.data.repository

import android.content.Context
import androidx.credentials.CredentialManager
import com.xbot.domain.models.enums.SocialType
import com.xbot.domain.repository.AuthRepository
import com.xbot.network.client.AnilibriaClient
import kotlinx.coroutines.flow.Flow

internal class AndroidAuthRepository(
    private val context: Context,
    private val credentialManager: CredentialManager,
    private val client: AnilibriaClient,
) : AuthRepository {

    override suspend fun login(login: String, password: String): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun logout(): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun socialLogin(provider: SocialType): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun forgotPassword(email: String): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun resetPassword(
        token: String,
        password: String,
        passwordConfirmation: String
    ): Result<Unit> {
        TODO("Not yet implemented")
    }

    override fun observeAuthState(): Flow<Boolean> {
        TODO("Not yet implemented")
    }
}