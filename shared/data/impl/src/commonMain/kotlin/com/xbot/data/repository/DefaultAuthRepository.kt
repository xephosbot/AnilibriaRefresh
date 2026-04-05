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
import com.xbot.network.client.SessionStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Singleton

@Singleton
internal class DefaultAuthRepository(
    private val authApi: AuthApi,
    private val tokenStorage: SessionStorage
) : AuthRepository {

    override val authState: Flow<Boolean> =
        (tokenStorage as DefaultSessionStorage).tokenFlow.map { !it.isNullOrBlank() }

    override suspend fun login(login: String, password: String): Either<DomainError, Unit> = either {
        val response = authApi.login(login, password)
            .mapLeft(NetworkError::toDomain)
            .bind()

        response.token?.let { tokenStorage.saveToken(it) }
    }

    override suspend fun logout(): Either<DomainError, Unit> = either {
        val result = authApi.logout()
            .mapLeft(NetworkError::toDomain)
        
        tokenStorage.clearToken()
        
        result.bind()
    }

    override suspend fun socialLogin(provider: SocialType): Either<DomainError, Unit> = either {
        val state = authApi.socialLogin(provider.toDto())
            .mapLeft(NetworkError::toDomain)
            .bind().state

        val response = authApi.socialAuthenticate(state)
            .mapLeft(NetworkError::toDomain)
            .bind()

        response.token?.let { tokenStorage.saveToken(it) }
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
