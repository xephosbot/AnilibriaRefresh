package com.xbot.domain.usecase

import com.xbot.common.DispatcherProvider
import com.xbot.domain.models.AuthState
import com.xbot.domain.repository.AuthRepository
import com.xbot.domain.repository.ProfileRepository
import kotlin.native.HiddenFromObjC
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Factory

@Factory
@HiddenFromObjC
class GetAuthStateUseCase(
    private val authRepository: AuthRepository,
    private val profileRepository: ProfileRepository,
    private val dispatcherProvider: DispatcherProvider
) : FlowUseCase<Unit, AuthState> {
    override fun invoke(params: Unit): Flow<AuthState> = authRepository.authState
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
