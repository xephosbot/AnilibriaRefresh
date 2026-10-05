package com.xbot.domain.usecase

import com.xbot.common.DispatcherProvider
import com.xbot.data.repository.AuthRepository
import com.xbot.data.repository.ProfileRepository
import com.xbot.domain.models.AuthState
import kotlin.native.HiddenFromObjC
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Factory

@Factory
@HiddenFromObjC
internal class DefaultGetAuthStateUseCase(
    private val authRepository: AuthRepository,
    private val profileRepository: ProfileRepository,
    private val dispatcherProvider: DispatcherProvider
) : GetAuthStateUseCase {
    override fun invoke(): Flow<AuthState> = authRepository.authState
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
