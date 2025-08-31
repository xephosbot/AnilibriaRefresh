package com.xbot.data.repository

import arrow.core.Either
import arrow.core.raise.either
import com.xbot.data.mapper.toDto
import com.xbot.data.mapper.toDomain
import com.xbot.domain.models.Error
import com.xbot.domain.models.enums.SocialType
import com.xbot.domain.repository.AuthRepository
import com.xbot.network.client.NetworkError
import com.xbot.network.api.AuthApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

internal class DefaultAuthRepository(
    private val authApi: AuthApi,
    private val tokenStorage: TokenStorage
): AuthRepository {
    override suspend fun login(login: String, password: String): Either<Error, Unit> = either {
        val token = authApi.login(login, password)
            .mapLeft(NetworkError::toDomain)
            .bind().token

        token?.let { token -> tokenStorage.saveToken(token) }
    }

    override suspend fun logout(): Either<Error, Unit> = either {
        authApi.logout()
            .mapLeft(NetworkError::toDomain)
            .bind()

        tokenStorage.clearToken()
    }

    override suspend fun socialLogin(provider: SocialType): Either<Error, Unit> = either {
        val response = authApi.socialLogin(provider.toDto())
            .mapLeft(NetworkError::toDomain)
            .bind()
        val token = authApi.socialAuthenticate(response.state)
            .mapLeft(NetworkError::toDomain)
            .bind().token

        token?.let { token -> tokenStorage.saveToken(token) }
    }

    override suspend fun forgotPassword(email: String): Either<Error, Unit> = either {
        authApi.forgotPassword(email)
            .mapLeft(NetworkError::toDomain)
            .bind()
    }

    override suspend fun resetPassword(
        token: String,
        password: String,
        passwordConfirmation: String
    ): Either<Error, Unit> = either {
        authApi.resetPassword(token, password, passwordConfirmation)
            .mapLeft(NetworkError::toDomain)
            .bind()
    }

    override fun observeAuthState(): Flow<Boolean> = flow {  }
}