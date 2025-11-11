package com.xbot.data.repository

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.raise.context.ensure
import arrow.core.raise.context.ensureNotNull
import arrow.core.raise.either
import com.xbot.data.mapper.toDomain
import com.xbot.data.mapper.toDto
import com.xbot.domain.models.DomainError
import com.xbot.domain.models.enums.SocialType
import com.xbot.domain.repository.AuthRepository
import com.xbot.network.api.AuthApi
import com.xbot.network.client.NetworkError
import com.xbot.network.client.TokenStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

internal class DefaultAuthRepository(
    private val authApi: AuthApi,
    private val tokenStorage: TokenStorage
) : AuthRepository {
    private val authMutex = Mutex()

    private val _authState: MutableStateFlow<Boolean> = MutableStateFlow(false)
    override val authState: Flow<Boolean> = _authState
        .onStart { init() }

    private suspend fun init() {
        val token = tokenStorage.getToken()
        _authState.update { token != null }
    }

    private suspend fun saveTokenAndUpdateState(token: String?): Either<DomainError, Unit> =
        either {
            ensureNotNull(token) { DomainError.UnknownError(IllegalArgumentException("Token is null")) }
            ensure(token.isNotBlank()) { DomainError.UnknownError(IllegalArgumentException("Token is blank")) }

            authMutex.withLock {
                tokenStorage.saveToken(token)
                init()
            }
        }

    override suspend fun login(login: String, password: String): Either<DomainError, Unit> =
        authApi.login(login, password)
            .mapLeft(NetworkError::toDomain)
            .flatMap {
                print("AUTHTOKEN: ${it.token}")
                saveTokenAndUpdateState(it.token)
            }

    override suspend fun logout(): Either<DomainError, Unit> =
        authApi.logout()
            .mapLeft(NetworkError::toDomain)
            .map {
                authMutex.withLock {
                    tokenStorage.clearToken()
                    _authState.update { false }
                }
            }

    override suspend fun socialLogin(provider: SocialType): Either<DomainError, Unit> = either {
        val state = authApi.socialLogin(provider.toDto())
            .mapLeft(NetworkError::toDomain)
            .bind().state

        val token = authApi.socialAuthenticate(state)
            .mapLeft(NetworkError::toDomain)
            .bind().token

        saveTokenAndUpdateState(token).bind()
    }

    override suspend fun forgotPassword(email: String): Either<DomainError, Unit> = authApi
        .forgotPassword(email)
        .mapLeft(NetworkError::toDomain)

    override suspend fun resetPassword(
        token: String,
        password: String,
        passwordConfirmation: String
    ): Either<DomainError, Unit> = authApi
        .resetPassword(token, password, passwordConfirmation)
        .mapLeft(NetworkError::toDomain)
}