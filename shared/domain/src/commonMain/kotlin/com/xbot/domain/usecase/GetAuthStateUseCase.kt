package com.xbot.domain.usecase

import com.xbot.domain.models.AuthState
import com.xbot.domain.repository.AuthRepository
import com.xbot.domain.repository.ProfileRepository
import com.xbot.domain.utils.DispatcherProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class GetAuthStateUseCase(
    private val authRepository: AuthRepository,
    private val profileRepository: ProfileRepository,
    private val dispatcherProvider: DispatcherProvider,
) {
    operator fun invoke(): Flow<AuthState> {
        return authRepository.authState
            .map { isAuthenticated ->
                if (isAuthenticated) {
                    profileRepository.getProfile().fold(
                        ifLeft = { error -> AuthState.Unauthenticated(error) },
                        ifRight = { user -> AuthState.Authenticated(user) }
                    )
                } else {
                    AuthState.Unauthenticated(null)
                }
            }
            .flowOn(dispatcherProvider.io)
    }
}
