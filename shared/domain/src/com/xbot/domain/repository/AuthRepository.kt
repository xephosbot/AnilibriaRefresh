package com.xbot.domain.repository

import com.xbot.domain.models.enums.SocialType
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(login: String, password: String): Result<Unit>
    suspend fun logout(): Result<Unit>
    suspend fun socialLogin(provider: SocialType): Result<Unit>
    suspend fun forgotPassword(email: String): Result<Unit>
    suspend fun resetPassword(token: String, password: String, passwordConfirmation: String): Result<Unit>
    fun observeAuthState(): Flow<Boolean>
}