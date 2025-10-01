package com.xbot.data.repository

import arrow.core.Either
import arrow.core.raise.either
import com.xbot.data.mapper.toDomain
import com.xbot.data.mapper.toDto
import com.xbot.domain.models.DomainError
import com.xbot.domain.models.enums.SocialType
import com.xbot.domain.repository.AuthRepository
import com.xbot.network.api.AuthApi
import com.xbot.network.client.NetworkError
import com.xbot.network.client.TokenStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

internal class DefaultAuthRepository(
    private val authApi: AuthApi,
    private val tokenStorage: TokenStorage
): AuthRepository {
    private val authState = MutableStateFlow(false)
    private val scope = CoroutineScope(Dispatchers.Default)

    init {
        scope.launch {
            authState.value = tokenStorage.getToken() != null
        }
    }

    override suspend fun login(login: String, password: String): Either<DomainError, Unit> = either {
        val token = authApi.login(login, password)
            .mapLeft(NetworkError::toDomain)
            .bind().token

        token?.let { token ->
            tokenStorage.saveToken(token)
            authState.value = true
        }
    }

    override suspend fun logout(): Either<DomainError, Unit> = either {
        authApi.logout()
            .mapLeft(NetworkError::toDomain)
            .bind()

        tokenStorage.clearToken()
        authState.value = false
    }

    override suspend fun socialLogin(provider: SocialType): Either<DomainError, Unit> = either {
        val response = authApi.socialLogin(provider.toDto())
            .mapLeft(NetworkError::toDomain)
            .bind()
        val token = authApi.socialAuthenticate(response.state)
            .mapLeft(NetworkError::toDomain)
            .bind().token

        token?.let { token ->
            tokenStorage.saveToken(token)
            authState.value = true
        }
    }

    override suspend fun forgotPassword(email: String): Either<DomainError, Unit> = authApi
        .forgotPassword(email)
        .mapLeft(NetworkError::toDomain)

    override suspend fun resetPassword(
        token: String,
        password: String,
        passwordConfirmation: String
    ): Either<DomainError, Unit> =  authApi
        .resetPassword(token, password, passwordConfirmation)
        .mapLeft(NetworkError::toDomain)

    override fun observeAuthState(): Flow<Boolean> = authState
}