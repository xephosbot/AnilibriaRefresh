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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class DefaultAuthRepository(
    private val authApi: AuthApi,
    private val tokenStorage: TokenStorage
) : AuthRepository {

    override val authState: Flow<Boolean> =
        tokenStorage.tokenFlow.map { it != null }

    override suspend fun login(login: String, password: String): Either<DomainError, Unit> =
        authApi.login(login, password)
            .mapLeft(NetworkError::toDomain)
            .map {}

    override suspend fun logout(): Either<DomainError, Unit> =
        authApi.logout()
            .mapLeft(NetworkError::toDomain)
            .map {}

    override suspend fun socialLogin(provider: SocialType): Either<DomainError, Unit> = either {
        val state = authApi.socialLogin(provider.toDto())
            .mapLeft(NetworkError::toDomain)
            .bind().state

        authApi.socialAuthenticate(state)
            .mapLeft(NetworkError::toDomain)
            .bind()
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
